package com.simpolab.server_main.db.das;

import com.simpolab.server_main.db.SessionDAO;
import com.simpolab.server_main.elector.domain.Elector;
import com.simpolab.server_main.voting_session.domain.VotingSession;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SessionDAS implements SessionDAO {

  private final JdbcTemplate jdbcTemplate;

  @Override
  public void create(VotingSession newSession) throws SQLException {
    try {
      String query =
        "INSERT INTO voting_session (id, name, type, ends_on, is_active, is_cancelled, need_absolute_majority, has_quorum) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
      jdbcTemplate.update(
        query,
        newSession.getId(),
        newSession.getName(),
        newSession.getType().name(),
        newSession.getEndsOn(),
        newSession.isActive(),
        newSession.isCancelled(),
        newSession.isNeedAbsoluteMajority(),
        newSession.isHasQuorum()
      );
    } catch (Exception e) {
      log.error("Failed to create new voting session");
      throw new SQLException("Failed to create new voting session", e);
    }
    log.info("Voting session created successfully");
  }

  @Override
  public void delete(long id) {
    var query = "DELETE FROM voting_session WHERE id = ? AND is_active = 0 AND is_cancelled = 0";
    try {
      jdbcTemplate.update(query, id);
      log.info("Voting session {} removed successfully", id);
    } catch (Exception e) {
      log.error("Failed to remove voting session", e);
    }
  }

  @Override
  public void addGroup(long sessionId, long groupId) throws SQLException {
    try {
      var query = "INSERT INTO session_group (voting_session_id, voting_group_id) VALUES (?, ?)";
      jdbcTemplate.update(query, sessionId, groupId);
      log.info("Group {} added to the session {} successfully", groupId, sessionId);
    } catch (Exception e) {
      log.error("Failed to add group {} to the session {}", groupId, sessionId);
      throw new SQLException("Failed to add elector to group", e);
    }
  }

  @Override
  public void removeGroup(long sessionId, long groupId) {
    try {
      var query = "DELETE FROM session_group WHERE voting_session_id = ? AND voting_group_id = ?";
      jdbcTemplate.update(query, sessionId, groupId);
      log.info("Group {} removed from session {} successfully", groupId, sessionId);
    } catch (Exception e) {
      log.error("Failed to remove group {} from session {}", groupId, sessionId, e);
    }
  }

  @Override
  public void createOption() throws SQLException {}

  @Override
  public void createOption(long parentOptionId) throws SQLException {}

  @Override
  public void deleteOption(long optionId) {}

  @Override
  public void setIsActive(long sessionId, boolean newValue) throws SQLException {}

  @Override
  public void setCancelled(long sessionId) throws SQLException {}
}
