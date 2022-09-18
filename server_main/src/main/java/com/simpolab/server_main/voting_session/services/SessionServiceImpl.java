package com.simpolab.server_main.voting_session.services;

import com.simpolab.server_main.db.GroupDAO;
import com.simpolab.server_main.db.SessionDAO;
import com.simpolab.server_main.elector.domain.Elector;
import com.simpolab.server_main.elector.services.ElectorService;
import com.simpolab.server_main.group.domain.Group;
import com.simpolab.server_main.voting_session.domain.*;
import com.simpolab.server_main.voting_session.domain.VotingSession.Type;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

  private final SessionDAO sessionDAO;
  private final GroupDAO groupDAO;
  private final ElectorService electorService;

  @Override
  public VotingSession newSession(VotingSession newSession) {
    try {
      long sessionId = sessionDAO.create(newSession);
      return getSession(sessionId);
    } catch (DuplicateKeyException e) {
      throw e;
    } catch (SQLException e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public void deleteSession(long sessionId) {
    try {
      sessionDAO.delete(sessionId);
    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public void addGroup(long sessionId, long groupId) {
    try {
      sessionDAO.addGroup(sessionId, groupId);
    } catch (SQLException e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public void removeGroup(long sessionId, long groupId) {
    sessionDAO.removeGroup(sessionId, groupId);
  }

  @Override
  public List<Group> getGroups(long sessionId) {
    return groupDAO.getGroupsForSession(sessionId);
  }

  @Override
  public void newOption(long votingSessionId, String optionValue) {
    try {
      sessionDAO.createOption(votingSessionId, optionValue);
    } catch (SQLException e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public void newOption(long votingSessionId, String optionValue, long parentOptionId) {
    try {
      sessionDAO.createOption(votingSessionId, optionValue, parentOptionId);
    } catch (SQLException e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public void removeOption(long optionId) {
    try {
      sessionDAO.deleteOption(optionId);
    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public List<VotingOption> getOptions(long sessionId) {
    return sessionDAO.getOptions(sessionId);
  }

  public void setState(long sessionId, VotingSession.State newState) {
    try {
      var session = getSession(sessionId);
      var options = getOptions(sessionId);
      if (
        options.isEmpty() && newState == VotingSession.State.ACTIVE
      ) throw new IllegalStateException("Cannot start a session without options");

      // add yes and no options in case of referendum session
      if (session.getType() == Type.REFERENDUM && newState == VotingSession.State.ACTIVE) {
        log.debug("[Set State] - session is referendum, adding yes and no options");
        var parentOptionId = options.get(0).getId();
        newOption(sessionId, "Yes", parentOptionId);
        newOption(sessionId, "No", parentOptionId);
        log.debug("[Set State] - session is referendum, options added successfully");
      }

      if (newState == VotingSession.State.ACTIVE) {
        log.debug("[Set State] - session is being activated, adding participants");
        sessionDAO.populateSessionParticipants(sessionId);
        log.debug("[Set State] - participants added successfully");
      }

      if (newState == VotingSession.State.ENDED) {
        // determine winner
        log.debug("[Set State] - session is being activated, adding participants");
        sessionDAO.populateSessionParticipants(sessionId);
        log.debug("[Set State] - participants added successfully");
      }

      log.debug("[Set State] - setting state");
      sessionDAO.setState(sessionId, newState);
    } catch (Exception e) {
      log.error("[Set State] - FAILED: {}", e.getMessage());
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public void expressVote(String electorUsername, long sessionId, List<Vote> votes) {
    try {
      log.info("Votes: {}", votes);

      // get the id of the elector startin from the username
      Optional<Elector> optElectorId = electorService.getElectorByUsername(electorUsername);
      if (optElectorId.isEmpty()) throw new IllegalArgumentException(
        "The given elector doesn't exist"
      );

      var electorId = optElectorId.get().getUser().getId();

      // check session is active
      var session = getSession(sessionId);
      if (!session.isActive()) throw new IllegalStateException("Session is not active");

      // check elector can vote in the session
      Optional<Boolean> optHasVoted = sessionDAO.getParticipationStatus(sessionId, electorId);
      log.info("Has voted: {}", optHasVoted);
      if (optHasVoted.isEmpty() || optHasVoted.get()) throw new IllegalArgumentException(
        "This user cannot vote in the session"
      );

      // check if vote is valid for the voting type
      var sessionType = session.getType();
      var options = sessionDAO.getOptionsForSession(sessionId);
      log.debug("All Options: {}", options);
      var voteIsValid = VoteValidator.validateVotes(sessionType, votes, options);
      if (!voteIsValid) throw new IllegalArgumentException(
        "The given votes are not valid for the session"
      );

      // add vote to votes
      sessionDAO.addVotes(sessionId, votes);

      // set has voted to true
      sessionDAO.setHasVoted(sessionId, electorId);
    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public VotingSession getSession(long sessionId) {
    var optSession = sessionDAO.get(sessionId);
    return optSession.isEmpty() ? null : optSession.get();
  }

  @Override
  public List<VotingSession> getAllSessions() {
    return sessionDAO.getAll();
  }

  @Override
  public List<VotingSession> getAllSessions(String electorUsername) {
    Optional<Elector> optElectorId = electorService.getElectorByUsername(electorUsername);
    if (optElectorId.isEmpty()) throw new IllegalArgumentException(
      "The given elector doesn't exist"
    );

    var electorId = optElectorId.get().getUser().getId();

    return sessionDAO.getAll(electorId);
  }

  @Override
  public Map<Long, Integer> votesPerOption(long sessionId) {
    // per voto ordinale e' il numero di volte che e' stato messo al primo posto
    // per gli altri il conteggio va bene
    val optSession = sessionDAO.get(sessionId);
    if (optSession.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

    val session = optSession.get();
    if (session.getState() != VotingSession.State.ENDED) throw new ResponseStatusException(
      HttpStatus.FORBIDDEN
    );

    val options = sessionDAO.getOptions(sessionId);

    Map<Long, Integer> votes;
    if (session.getType() == Type.ORDINAL) votes =
      sessionDAO.getVotesPerOptionOrdinal(sessionId, List.of()); else votes =
      sessionDAO.getVotesPerOption(sessionId);

    var optsIdsSet = options.stream().map(VotingOption::getId).collect(Collectors.toSet());
    optsIdsSet.removeAll(votes.keySet());
    optsIdsSet.forEach(id -> votes.put(id, 0));

    return votes;
  }

  @Override
  public List<Long> getWinner(long sessionId) throws NoWinnerException {
    val optSession = sessionDAO.get(sessionId);
    if (optSession.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

    val session = optSession.get();
    if (session.getState() != VotingSession.State.ENDED) throw new ResponseStatusException(
      HttpStatus.FORBIDDEN
    );

    val stats = sessionDAO.getParticipationStats(sessionId);

    // ** Quorum Check
    if (session.isHasQuorum() && !hasReachedQuorum(stats)) {
      log.warn("Quorum not met for session {}, cancelling it", sessionId);
      setState(sessionId, VotingSession.State.CANCELLED);
      throw new NoWinnerException(NoWinnerException.QUORUM_NOT_REACHED);
    }
    // ****

    switch (session.getType()) {
      case REFERENDUM, CATEGORIC -> {
        log.debug("REFERENDUM | CATEGORIC");

        Map<Long, Integer> votes = sessionDAO.getVotesPerOption(sessionId);
        if (votes.isEmpty()) throw new IllegalStateException("No votes found");

        log.debug("All VOTES: {}", votes);

        val mostVotedOptionId = getMostVotesOption(votes);
        val countMostVotedOption = countVotesForOption(votes, mostVotedOptionId);

        // check ballottaggio
        if (countMostVotedOption > 1) {
          log.debug("Ballottaggio, non c'e' vincitore");
          setState(sessionId, VotingSession.State.CANCELLED);
          throw new NoWinnerException(NoWinnerException.BALLOTTAGGIO);
        }

        // check absolute majority
        if (
          session.isNeedAbsoluteMajority() &&
          !hasReachedAbsoluteMajority(stats.getVotersCount(), votes.get(mostVotedOptionId))
        ) {
          log.warn("Absolute majority not met for session {}, cancelling it", sessionId);
          setState(sessionId, VotingSession.State.CANCELLED);
          throw new NoWinnerException(NoWinnerException.NO_ABSOLUTE_MAJORITY);
        }

        log.debug("Winner: {}", mostVotedOptionId);
        return List.of(mostVotedOptionId);
      }
      case ORDINAL -> {
        log.debug("ORDINAL");

        List<Long> excludedOptions = new ArrayList<>();
        while (true) {
          Map<Long, Integer> votes = sessionDAO.getVotesPerOptionOrdinal(
            sessionId,
            excludedOptions
          );
          if (votes.isEmpty()) throw new IllegalStateException("No votes found");

          log.debug("All VOTES: {}", votes);
          val mostVotedOptionId = getMostVotesOption(votes);
          val countMostVotedOption = countVotesForOption(votes, mostVotedOptionId);

          if (countMostVotedOption > 1) {
            log.debug("Ballottaggio ordinale");

            // get id of option with the lowest amount of votes
            val leastVotedOptionId = getLeastVotesOption(votes);

            // exclude it
            excludedOptions.add(leastVotedOptionId);

            // try again
            continue;
          }

          // check absolute majority
          if (
            session.isNeedAbsoluteMajority() &&
            !hasReachedAbsoluteMajority(stats.getVotersCount(), votes.get(mostVotedOptionId))
          ) {
            log.warn("Absolute majority not met for session {}, cancelling it", sessionId);
            setState(sessionId, VotingSession.State.CANCELLED);
            throw new NoWinnerException(NoWinnerException.NO_ABSOLUTE_MAJORITY);
          }

          log.debug("Winner: {}", mostVotedOptionId);
          return List.of(mostVotedOptionId);
        }
      }
      case CATEGORIC_WITH_PREFERENCES -> {
        log.debug("CATEGORIC_WITH_PREFERENCES");

        // ** 1 dividi opzioni ottieni top level options
        val options2 = sessionDAO.getOptionsForSession(sessionId);
        var topLevelOptions = new HashSet<Long>();

        options2.forEach(opt -> {
          if (opt.parentId() == 0) topLevelOptions.add(opt.id());
        });
        // **** 1

        // ** 2 ottieni tutti i voti per la sessione
        Map<Long, Integer> votes = sessionDAO.getVotesPerOption(sessionId);
        if (votes.isEmpty()) throw new IllegalStateException("No votes found");

        Map<Long, Integer> topLevelVotes = new HashMap<>();
        Map<Long, Integer> lowLevelVotes = new HashMap<>();

        votes.forEach((k, v) -> {
          if (topLevelOptions.contains(k)) topLevelVotes.put(k, v); else lowLevelVotes.put(k, v);
        });
        // **** 2

        // ** 3 identifica top level con maggior numero di voti
        val topMostVotedOptionId = getMostVotesOption(topLevelVotes);
        val topCountMostVotedOption = countVotesForOption(topLevelVotes, topMostVotedOptionId);

        // check absolute majority
        if (
          session.isNeedAbsoluteMajority() &&
          !hasReachedAbsoluteMajority(stats.getVotersCount(), votes.get(topMostVotedOptionId))
        ) {
          log.warn("Absolute majority not met for session {}, cancelling it", sessionId);
          setState(sessionId, VotingSession.State.CANCELLED);
          throw new NoWinnerException(NoWinnerException.NO_ABSOLUTE_MAJORITY);
        }

        // check ballottaggio
        if (topCountMostVotedOption > 1) {
          log.debug("Ballottaggio TOP level, non c'e' vincitore");
          setState(sessionId, VotingSession.State.CANCELLED);
          throw new NoWinnerException(NoWinnerException.BALLOTTAGGIO);
        }
        // **** 3

        // ** 4 identifica low level con maggior numero di voti
        val lowMostVotedOptionId = getMostVotesOption(lowLevelVotes);
        val lowCountMostVotedOption = countVotesForOption(lowLevelVotes, lowMostVotedOptionId);

        // check absolute majority
        if (
          session.isNeedAbsoluteMajority() &&
          !hasReachedAbsoluteMajority(stats.getVotersCount(), votes.get(lowMostVotedOptionId))
        ) {
          log.warn("Absolute majority not met for session {}, cancelling it", sessionId);
          setState(sessionId, VotingSession.State.CANCELLED);
          throw new NoWinnerException(NoWinnerException.NO_ABSOLUTE_MAJORITY);
        }

        // check ballottaggio
        if (lowCountMostVotedOption > 1) {
          log.debug("Ballottaggio LOW level, non c'e' vincitore");
          throw new NoWinnerException(
            NoWinnerException.BALLOTTAGGIO_CATEGORICO_PREFERENZE,
            topMostVotedOptionId
          );
        }
        // **** 4

        log.debug("Winners:  TOP - {}, LOW - {}", topMostVotedOptionId, lowMostVotedOptionId);
        return List.of(topMostVotedOptionId, lowMostVotedOptionId);
      }
    }

    throw new IllegalStateException("Should never be here");
  }

  private long getLeastVotesOption(Map<Long, Integer> votes) {
    val optRes = votes
      .entrySet()
      .stream()
      .reduce((entry, acc) -> entry.getValue() <= acc.getValue() ? entry : acc);

    if (optRes.isEmpty()) throw new IllegalStateException("No minimum found");

    return optRes.get().getKey();
  }

  private long getMostVotesOption(Map<Long, Integer> votes) {
    val optRes = votes
      .entrySet()
      .stream()
      .reduce((e, acc) -> e.getValue() >= acc.getValue() ? e : acc);

    if (optRes.isEmpty()) throw new IllegalStateException("No maximum found");

    return optRes.get().getKey();
  }

  private int countVotesForOption(Map<Long, Integer> votes, long optionId) {
    return (int) votes.values().stream().filter(v -> v == optionId).count();
  }

  private boolean hasReachedAbsoluteMajority(int votersCount, int obtainedVotes) {
    val threshold = votersCount / 2 + 1;

    return obtainedVotes >= threshold;
  }

  private boolean hasReachedQuorum(ParticipationStats participationStats) {
    val totalVoters = participationStats.getVotersCount() + participationStats.getNonVotersCount();
    val threshold = totalVoters / 2 + 1;

    return participationStats.getVotersCount() >= threshold;
  }
}
