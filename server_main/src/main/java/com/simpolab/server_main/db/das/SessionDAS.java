package com.simpolab.server_main.db.das;

import com.simpolab.server_main.db.SessionDAO;
import com.simpolab.server_main.voting_session.domain.Vote;
import com.simpolab.server_main.voting_session.domain.VotingSession;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SessionDAS implements SessionDAO {

  private final JdbcTemplate jdbcTemplate;

  private final RowMapper<Long> electorIdMapper = (rs, _ignore) -> rs.getLong("elector_id");

  private final RowMapper<VotingSession> votingSessionRowMapper = (rs, _ignore) ->
    VotingSession
      .builder()
      .id(rs.getLong("id"))
      .name(rs.getString("name"))
      .endsOn(rs.getDate("ends_on"))
      .state(VotingSession.State.valueOf(rs.getString("state")))
      .type(VotingSession.Type.valueOf(rs.getString("type")))
      .needAbsoluteMajority(rs.getBoolean("need_absolute_majority"))
      .hasQuorum(rs.getBoolean("has_quorum"))
      .build();

  public record Touple(long id, Long parentId) {}

  private final RowMapper<Touple> votingOptionMapper = (rs, _ignore) ->
    new Touple(rs.getLong("id"), rs.getLong("parent_option_id"));

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

  public void setState(long sessionId, VotingSession.State newState) throws SQLException {
    try {
      String query = "UPDATE voting_session SET state = ? WHERE id = ?";
      jdbcTemplate.update(query, newState.name(), sessionId);

      log.debug("[Status Update] - Status of session {} updated to {}", sessionId, newState);
    } catch (Exception e) {
      log.error("Failed to update the status of the session {} to {}", sessionId, newState);
      throw new SQLException("Failed update the status of the session", e);
    }
  }

  private List<Long> getElectorsOfSessionGroups(long sessionId) {
    try {
      var query =
        "SELECT elector_id FROM session_group AS sg, elector_group as eg WHERE sg.voting_group_id = eg.voting_group_id AND sg.voting_session_id = ?";
      return jdbcTemplate.query(query, electorIdMapper, sessionId);
    } catch (Exception e) {
      log.warn(e.getMessage());
      return new ArrayList<>();
    }
  }

  private void insertElectorsInSessionParticipation(List<Long> electors, long sessionId)
    throws SQLException {
    try {
      String sql =
        "INSERT INTO session_participation (elector_id, voting_session_id, has_voted) VALUES (?, ?, ?)";
      jdbcTemplate.batchUpdate(
        sql,
        new BatchPreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setLong(1, electors.get(i));
            ps.setLong(2, sessionId);
            ps.setBoolean(3, false);
          }

          @Override
          public int getBatchSize() {
            return electors.size();
          }
        }
      );
    } catch (Exception e) {
      log.error("Failed to add electors to session participation", e);
      throw new SQLException("Failed to add electors to session participation", e);
    }
  }

  @Override
  public void populateSessionParticipants(long sessionId) throws SQLException {
    var electors = getElectorsOfSessionGroups(sessionId);
    if (electors.isEmpty()) throw new IllegalStateException(
      "No electors where selected for the session " + sessionId
    );

    insertElectorsInSessionParticipation(electors, sessionId);
  }

  @Override
  public Optional<VotingSession> get(long id) {
    try {
      var query = "SELECT * FROM voting_session WHERE id = ?";
      VotingSession vs = jdbcTemplate.queryForObject(query, votingSessionRowMapper, id);
      return Optional.ofNullable(vs);
    } catch (Exception e) {
      log.warn(e.getMessage());
      return Optional.empty();
    }
  }

  @Override
  public List<VotingSession> getAll() {
    try {
      var query = "SELECT * FROM voting_session";
      return jdbcTemplate.query(query, votingSessionRowMapper);
    } catch (Exception e) {
      log.warn(e.getMessage());
      return List.of();
    }
  }

  public Optional<Boolean> getParticipationStatus(long sessionId, long electorId) {
    try {
      var query =
        "SELECT has_voted FROM session_participation WHERE elector_id = ? AND voting_session_id = ?";

      return Optional.ofNullable(
        jdbcTemplate.queryForObject(query, Boolean.class, electorId, sessionId)
      );
    } catch (Exception e) {
      log.warn(e.getMessage());
      return Optional.empty();
    }
  }

  public synchronized void addVotes(long sessionId, List<Vote> votes) throws SQLException {
    try {
      Long maxId = jdbcTemplate.queryForObject("SELECT MAX(id) FROM vote", Long.class);
      long newId = maxId == null ? 1L : maxId + 1;

      String sql = "INSERT INTO vote (id, voting_option_id, order_idx) VALUES (?, ?, ?)";
      jdbcTemplate.batchUpdate(
        sql,
        new BatchPreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            var vote = votes.get(i);

            ps.setLong(1, newId);
            ps.setLong(2, vote.getOptionId());
            ps.setLong(3, vote.getOrderIndex());
          }

          @Override
          public int getBatchSize() {
            return votes.size();
          }
        }
      );
    } catch (Exception e) {
      log.error("Failed to add votes to session", e);
      throw new SQLException("Failed to add votes to session", e);
    }
  }

  public void setHasVoted(long sessionId, long electorId) throws SQLException {
    try {
      String query =
        "UPDATE session_participation SET has_voted = 1 WHERE voting_session_id = ? AND elector_id = ?";
      jdbcTemplate.update(query, sessionId, electorId);
    } catch (Exception e) {
      log.error("Failed set has voted for elector {} in session {}", electorId, sessionId);
      throw new SQLException("Failed set has voted for elector in session", e);
    }
    log.info("Elector {} has voted successfully in session {}", electorId, sessionId);
  }

  @Override
  public List<Touple> getOptionsForSession(long sessionId) {
    try {
      var query =
        "SELECT id, parent_option_id FROM voting_option AS vo  WHERE vo.voting_session_id = ?";
      return jdbcTemplate.query(query, votingOptionMapper, sessionId);
    } catch (Exception e) {
      log.warn(e.getMessage());
      return new ArrayList<>();
    }
  }
}
