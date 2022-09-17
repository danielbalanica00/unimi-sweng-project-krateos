package com.simpolab.server_main.voting_session.services;

import com.simpolab.server_main.db.GroupDAO;
import com.simpolab.server_main.db.SessionDAO;
import com.simpolab.server_main.elector.domain.Elector;
import com.simpolab.server_main.elector.services.ElectorService;
import com.simpolab.server_main.group.domain.Group;
import com.simpolab.server_main.voting_session.VoteValidator;
import com.simpolab.server_main.voting_session.domain.Vote;
import com.simpolab.server_main.voting_session.domain.VotingOption;
import com.simpolab.server_main.voting_session.domain.VotingSession;
import com.simpolab.server_main.voting_session.domain.VotingSession.Type;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

  //  @Autowired
  private final SessionDAO sessionDAO;

  //  @Autowired
  private final GroupDAO groupDAO;

  //  @Autowired
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
      if (options.isEmpty()) throw new IllegalStateException(
        "Cannot start a session without options"
      );

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

  public Map<Long, Integer> votesPerOption(long sessionId) {
    // per voto ordinale e' il numero di volte che e' stato messo al primo posto
    // per gli altri il conteggio va bene
    val session = sessionDAO.get(sessionId).get();
    val options = sessionDAO.getOptions(sessionId);

    Map<Long, Integer> votes = sessionDAO.getVotesPerOption(session.getType(), sessionId);

    var optsIdsSet = options.stream().map(VotingOption::getId).collect(Collectors.toSet());
    optsIdsSet.removeAll(votes.keySet());
    optsIdsSet.forEach(id -> votes.put(id, 0));

    return votes;
  }

  @Override
  public void determineWinner(VotingSession session, List<VotingOption> options) {
    Map<Long, Integer> votes = sessionDAO.getVotesPerOption(session.getType(), session.getId());
    log.debug("All VOTES: {}", votes);

    var winner = votes
      .entrySet()
      .stream()
      .reduce((e, acc) -> e.getValue() > acc.getValue() ? e : acc);
    if (winner.isEmpty()) {
      throw new IllegalStateException("No winner found");
    }

    var optsIdsSet = options.stream().map(VotingOption::getId).collect(Collectors.toSet());
    optsIdsSet.removeAll(votes.keySet());
    optsIdsSet.forEach(id -> votes.put(id, 0));
    log.debug("Non voted options: {}", optsIdsSet);
    log.debug("All Options with votes: {}", votes);

    log.debug("Winner: {}", winner);

    switch (session.getType()) {
      case REFERENDUM -> {
        log.debug("REFERENDUM");
      }
      case CATEGORIC -> {
        log.debug("CATEGORIC");
      }
      case ORDINAL -> {
        log.debug("ORDINAL");
      }
      case CATEGORIC_WITH_PREFERENCES -> {
        log.debug("CATEGORIC_WITH_PREFERENCES");
      }
    }
    // CHECK if quorum is present and if then if it is set

    // REFERENDUM
    // vince si o no in base al numero di voti

    // CATEGORICO && CATEGORICO con PREFERENZE
    // vince quello con piu voti

    // ORDINALE
    // vince quello messo piu volte al primo posto

    // check se e' settata la maggioranza assoluta.
  }
}
