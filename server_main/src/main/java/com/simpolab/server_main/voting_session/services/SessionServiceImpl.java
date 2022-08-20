package com.simpolab.server_main.voting_session.services;

import com.simpolab.server_main.db.SessionDAO;
import com.simpolab.server_main.voting_session.domain.VotingSession;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SessionServiceImpl implements SessionService {

  @Autowired
  private SessionDAO sessionDAO;

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
}
