package com.simpolab.server_main.db;

import com.simpolab.server_main.db.das.SessionDAS;
import com.simpolab.server_main.voting_session.domain.Vote;
import com.simpolab.server_main.voting_session.domain.VotingSession;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface SessionDAO {
  long create(VotingSession newSession) throws SQLException;
  void delete(long id);

  Optional<VotingSession> get(long id);

  List<VotingSession> getAll();

  void addGroup(long sessionId, long groupId) throws SQLException;
  void removeGroup(long sessionId, long groupId);

  void createOption(long votingSessionId, String optionValue) throws SQLException;

  void createOption(long votingSessionId, String optionValue, long parentOptionId)
    throws SQLException;

  void deleteOption(long optionId);

  List<SessionDAS.Touple> getOptionsForSession(long sessionId);

  void setActive(long sessionId) throws SQLException;

  void setCancelled(long sessionId) throws SQLException;
  void setEnded(long sessionId) throws SQLException;

  void setState(long sessionId, VotingSession.State newState) throws SQLException;

  void populateSessionParticipants(long sessionId) throws SQLException;

  Optional<Boolean> getParticipationStatus(long sessionId, long electorId);

  void addVotes(long sessionId, List<Vote> votes) throws SQLException;

  void setHasVoted(long sessionId, long electorId) throws SQLException;
}
