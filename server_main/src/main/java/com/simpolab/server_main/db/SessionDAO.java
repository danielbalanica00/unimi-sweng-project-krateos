package com.simpolab.server_main.db;

import com.simpolab.server_main.voting_session.domain.VotingSession;
import java.sql.SQLException;

public interface SessionDAO {
  void create(VotingSession newSession) throws SQLException;
  void delete(long id);

  void addGroup(long sessionId, long groupId) throws SQLException;
  void removeGroup(long sessionId, long groupId);

  void createOption(long votingSessionId, String optionValue) throws SQLException;

  void createOption(long votingSessionId, String optionValue, long parentOptionId)
    throws SQLException;

  void deleteOption(long optionId);

  void setActive(long sessionId) throws SQLException;

  void setCancelled(long sessionId) throws SQLException;
  void setEnded(long sessionId) throws SQLException;
}
