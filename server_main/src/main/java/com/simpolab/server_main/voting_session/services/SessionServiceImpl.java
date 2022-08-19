package com.simpolab.server_main.voting_session.services;

import com.simpolab.server_main.db.SessionDAO;
import com.simpolab.server_main.voting_session.domain.VotingOption;
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
  public void addGroup(long sessionId, long groupId) {}

  @Override
  public void removeGroup(long sessionId, long groupId) {}

  @Override
  public void newOption(VotingOption newOption) {}

  @Override
  public void newOption(VotingOption newOption, long parentOptionId) {}

  @Override
  public void removeOption(long optionId) {}

  @Override
  public void startSession(long sessionId) {}

  @Override
  public void endSession(long sessionId) {}

  @Override
  public void cancelSession(long sessionId) {}
}
