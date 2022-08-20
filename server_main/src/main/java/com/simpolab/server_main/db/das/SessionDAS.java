package com.simpolab.server_main.db.das;

import com.simpolab.server_main.db.SessionDAO;
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
        "INSERT INTO voting_session (id, name, type, ends_on, need_absolute_majority, has_quorum) VALUES (?, ?, ?, ?, ?, ?)";
      jdbcTemplate.update(
        query,
        newSession.getId(),
        newSession.getName(),
        newSession.getType().name(),
        newSession.getEndsOn(),
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
  public void createOption(long votingSessionId, String optionValue) throws SQLException {
    try {
      String query = "INSERT INTO voting_option (voting_session_id, option_value) VALUES (?, ?)";
      jdbcTemplate.update(query, votingSessionId, optionValue);
    } catch (Exception e) {
      log.error("Failed to create new option");
      throw new SQLException("Failed to create new option", e);
    }
    log.info("Option created successfully");
  }

  @Override
  public void createOption(long votingSessionId, String optionValue, long parentOptionId)
    throws SQLException {
    try {
      String query =
        "INSERT INTO voting_option (voting_session_id, option_value, parent_option_id) VALUES (?, ?, ?)";
      jdbcTemplate.update(query, votingSessionId, optionValue, parentOptionId);
    } catch (Exception e) {
      log.error("Failed to create new option as children of {}", parentOptionId);
      throw new SQLException("Failed to create new option", e);
    }
    log.info("Option created successfully as children of {}", parentOptionId);
  }

  @Override
  public void deleteOption(long optionId) {
    // todo allow only if is not active
    var query = "DELETE FROM voting_option WHERE id = ?";
    try {
      jdbcTemplate.update(query, optionId);
      log.info("Voting option {} removed successfully", optionId);
    } catch (Exception e) {
      log.error("Failed to remove voting session", e);
    }
  }

  @Override
  public void setActive(long sessionId) throws SQLException {
    try {
      String query = "UPDATE voting_session SET is_active = 1 WHERE id = ?";
      jdbcTemplate.update(query, sessionId);
    } catch (Exception e) {
      log.error("Failed to set session {} to active", sessionId);
      throw new SQLException("Failed set session to active", e);
    }
    log.info("Session {} is now active", sessionId);
  }

  @Override
  public void setCancelled(long sessionId) throws SQLException {
    try {
      String query = "UPDATE voting_session SET is_cancelled = 1 WHERE id = ?";
      jdbcTemplate.update(query, sessionId);
    } catch (Exception e) {
      log.error("Failed to cancel session {}", sessionId);
      throw new SQLException("Failed to cancel session", e);
    }
    log.info("Session {} is now cancelled", sessionId);
  }

  @Override
  public void setEnded(long sessionId) throws SQLException {
    try {
      String query = "UPDATE voting_session SET has_ended = 1 WHERE id = ?";
      jdbcTemplate.update(query, sessionId);
    } catch (Exception e) {
      log.error("Failed to end session {}", sessionId);
      throw new SQLException("Failed to end session", e);
    }
    log.info("Session {} is now ended", sessionId);
  }
}
