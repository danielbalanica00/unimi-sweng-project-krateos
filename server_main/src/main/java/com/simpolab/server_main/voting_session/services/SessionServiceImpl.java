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
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

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
      // todo get the state and check if possible
      sessionDAO.setState(sessionId, newState);

      if (newState == VotingSession.State.ACTIVE) {
        //        sessionDAO.populateSessionParticipants(sessionId);
      }
    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public void startSession(long sessionId) {
    try {
      sessionDAO.setActive(sessionId);
      sessionDAO.populateSessionParticipants(sessionId);
    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public void endSession(long sessionId) {
    try {
      sessionDAO.setEnded(sessionId);
    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public void cancelSession(long sessionId) {
    try {
      sessionDAO.setCancelled(sessionId);
    } catch (Exception e) {
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
}
