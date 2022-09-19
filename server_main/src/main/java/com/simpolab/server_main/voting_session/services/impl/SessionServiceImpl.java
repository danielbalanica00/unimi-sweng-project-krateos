package com.simpolab.server_main.voting_session.services.impl;

import com.simpolab.server_main.db.GroupDAO;
import com.simpolab.server_main.db.SessionDAO;
import com.simpolab.server_main.elector.domain.NewElector;
import com.simpolab.server_main.elector.services.ElectorService;
import com.simpolab.server_main.group.domain.Group;
import com.simpolab.server_main.voting_session.domain.NoWinnerException;
import com.simpolab.server_main.voting_session.domain.Vote;
import com.simpolab.server_main.voting_session.domain.VotingOption;
import com.simpolab.server_main.voting_session.domain.VotingSession;
import com.simpolab.server_main.voting_session.domain.VotingSession.Type;
import com.simpolab.server_main.voting_session.services.SessionService;
import com.simpolab.server_main.voting_session.utils.VoteValidator;
import com.simpolab.server_main.voting_session.utils.WinnerElection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    Optional<NewElector> optElectorId = electorService.getElectorByUsername(electorUsername);
    if (optElectorId.isEmpty()) throw new IllegalArgumentException(
      "The given elector doesn't exist"
    );

    var electorId = optElectorId.get().getId();

    return sessionDAO.getAll(electorId);
  }

  @Override
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

      log.debug("[Set State] - setting state");
      sessionDAO.setState(sessionId, newState);
    } catch (Exception e) {
      log.error("[Set State] - FAILED: {}", e.getMessage());
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public Map<Long, Integer> votesPerOption(long sessionId) {
    // per voto ordinale e' il numero di volte che e' stato messo al primo posto
    // per gli altri il conteggio va bene
    val optSession = sessionDAO.get(sessionId);
    if (optSession.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

    val session = optSession.get();
    if (
      session.getState() == VotingSession.State.INACTIVE ||
      session.getState() == VotingSession.State.ACTIVE ||
      session.getState() == VotingSession.State.CANCELLED
    ) throw new ResponseStatusException(HttpStatus.FORBIDDEN);

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
    return WinnerElection.getWinner(this, sessionDAO, sessionId);
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

  @Override
  public void expressVote(String electorUsername, long sessionId, List<Vote> votes) {
    try {
      log.info("Votes: {}", votes);

      // get the id of the elector startin from the username
      Optional<NewElector> optElectorId = electorService.getElectorByUsername(electorUsername);
      if (optElectorId.isEmpty()) throw new IllegalArgumentException(
        "The given elector doesn't exist"
      );

      var electorId = optElectorId.get().getId();

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
}
