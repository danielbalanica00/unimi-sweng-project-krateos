package com.simpolab.server_main.voting_session.services;

import com.simpolab.server_main.db.SessionDAO;
import com.simpolab.server_main.elector.domain.Elector;
import com.simpolab.server_main.elector.services.ElectorService;
import com.simpolab.server_main.voting_session.domain.Vote;
import com.simpolab.server_main.voting_session.domain.VotingSession;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SessionServiceImpl implements SessionService {

  @Autowired
  private SessionDAO sessionDAO;

  @Autowired
  private ElectorService electorService;

  @Override
  public void newSession(VotingSession newSession) {
    try {
      sessionDAO.create(newSession);
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
      Optional<Boolean> optCanVote = sessionDAO.getParticipationStatus(sessionId, electorId);
      if (optCanVote.isEmpty() || !optCanVote.get()) throw new IllegalArgumentException(
        "This user cannot vote in the session"
      );

      // check if vote is valid for the voting type
      /*
       * HELLO
       */

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
    return sessionDAO.get(sessionId);
  }
}
