package com.simpolab.server_main.db.das;

import com.simpolab.server_main.db.SessionDAO;
import com.simpolab.server_main.voting_session.domain.ParticipationStats;
import com.simpolab.server_main.voting_session.domain.Vote;
import com.simpolab.server_main.voting_session.domain.VotingOption;
import com.simpolab.server_main.voting_session.domain.VotingSession;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SessionDAS implements SessionDAO {

  private final JdbcTemplate jdbcTemplate;

  private final RowMapper<Long> electorIdMapper = (rs, _ignore) -> rs.getLong("elector_id");

  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  private final RowMapper<VotingSession> votingSessionRowMapper = (rs, _ignore) ->
    VotingSession
      .builder()
      .id(rs.getLong("id"))
      .name(rs.getString("name"))
      .endsOn(new Date(rs.getTimestamp("ends_on").getTime()))
      .state(VotingSession.State.valueOf(rs.getString("state")))
      .type(VotingSession.Type.valueOf(rs.getString("type")))
      .needAbsoluteMajority(rs.getBoolean("need_absolute_majority"))
      .hasQuorum(rs.getBoolean("has_quorum"))
      .build();

  private final RowMapper<VotingOption> votingOptionRowMapper = (rs, _ignore) ->
    VotingOption
      .builder()
      .id(rs.getLong("id"))
      .value(rs.getString("option_value"))
      .parentOptionId(rs.getLong("parent_option_id"))
      .build();

  public record Touple(long id, Long parentId) {}

  private final RowMapper<Touple> votingOptionMapper = (rs, _ignore) ->
    new Touple(rs.getLong("id"), rs.getLong("parent_option_id"));

  @Override
  public long create(VotingSession newSession) throws SQLException {
    try {
      final String query =
        "INSERT INTO voting_session (name, type, ends_on, need_absolute_majority, has_quorum) VALUES (?, ?, ?, ?, ?)";

      //      jdbcTemplate.update(
      //        query,
      //        newSession.getName(),
      //        newSession.getType().name(),
      //        newSession.getEndsOn(),
      //        newSession.isNeedAbsoluteMajority(),
      //        newSession.isHasQuorum()
      //      );

      KeyHolder keyHolder = new GeneratedKeyHolder();

      var affectedRows = jdbcTemplate.update(
        connection -> {
          var ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
          ps.setString(1, newSession.getName());
          ps.setString(2, newSession.getType().name());
          ps.setTimestamp(3, Timestamp.from(newSession.getEndsOn().toInstant()));
          ps.setBoolean(4, newSession.isNeedAbsoluteMajority());
          ps.setBoolean(5, newSession.isHasQuorum());

          return ps;
        },
        keyHolder
      );

      if (affectedRows == 0) throw new SQLException("Failed to create new session");

      log.debug("Session created successfully");
      return keyHolder.getKey().longValue();
    } catch (Exception e) {
      log.error("Failed to create new voting session");
      throw new SQLException("Failed to create new voting session", e);
    }
  }

  @Override
  public void delete(long id) {
    var query = "DELETE FROM voting_session WHERE id = ? AND state = 'INACTIVE'";
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
  public List<VotingOption> getOptions(long votingSessionId) {
    try {
      var query = "SELECT * FROM voting_option WHERE voting_session_id = ?";
      return jdbcTemplate.query(query, votingOptionRowMapper, votingSessionId);
    } catch (Exception e) {
      log.warn(e.getMessage());
      return new ArrayList<>();
    }
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
        "INSERT IGNORE INTO session_participation (elector_id, voting_session_id, has_voted) VALUES (?, ?, ?)";
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

  @Override
  public List<VotingSession> getAll(long electorId) {
    try {
      val query =
        """
          (select vs.*
          from elector as e,
               elector_group as eg,
               session_group as sg,
               voting_session as vs
          where e.id = eg.elector_id
              and eg.voting_group_id = sg.voting_group_id
              and sg.voting_session_id = vs.id
              and e.id = ?)
          UNION DISTINCT
          (select vs.*
          from elector as e,
               session_participation as sp,
               voting_session as vs
          where e.id = sp.elector_id
              and sp.voting_session_id = vs.id
              and e.id = ?)
          """;

      return jdbcTemplate.query(query, votingSessionRowMapper, electorId, electorId);
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

  @Override
  public Map<Long, Integer> getVotesPerOption(long sessionId) {
    try {
      Map<Long, Integer> map = new HashMap<>();

      RowMapper<Void> mapper = (rs, _ignore) -> {
        map.put(rs.getLong("id"), rs.getInt("count"));
        return null;
      };

      val query =
        "select voting_option_id as id, count(*) as count from voting_option, vote where vote.voting_option_id = voting_option.id and voting_option.voting_session_id = ? group by voting_option_id";
      jdbcTemplate.query(query, mapper, sessionId);

      return map;
    } catch (Exception e) {
      log.warn(e.getMessage());
      return Map.of();
    }
  }

  @Override
  public Map<Long, Integer> getVotesPerOptionOrdinal(long sessionId, List<Long> excludedIds) {
    try {
      Map<Long, Integer> map = new HashMap<>();

      RowMapper<Void> mapper = (rs, _ignore) -> {
        map.put(rs.getLong("id"), rs.getInt("count"));
        return null;
      };

      List<Long> params = new ArrayList<>(excludedIds);
      params.add(0L);
      params.add(sessionId);

      String inSql = String.join(",", Collections.nCopies(excludedIds.size() + 1, "?"));
      String query = String.format(
        "with t as (select v.id, min(order_idx) as order_idx from vote as v where voting_option_id not in (%s) group by v.id) select voting_option_id as id, count(*) as count from vote, t, voting_option as vo where vote.order_idx = t.order_idx and vote.id = t.id and vo.id = vote.voting_option_id and vo.voting_session_id = ? group by voting_option_id",
        inSql
      );

      jdbcTemplate.query(query, mapper, params.toArray());

      log.debug("Got: {}", map);
      return map;
    } catch (Exception e) {
      log.warn(e.getMessage());
      return Map.of();
    }
  }

  @Override
  public ParticipationStats getParticipationStats(long sessionId) {
    try {
      val res = new ParticipationStats();
      val query =
        "select has_voted, count(*) as count from session_participation where voting_session_id = ? group by has_voted";

      jdbcTemplate.query(
        query,
        (rs, _ignore) -> {
          if (rs.getBoolean("has_voted")) res.setVotersCount(
            rs.getInt("count")
          ); else res.setNonVotersCount(rs.getInt("count"));
          return null;
        },
        sessionId
      );

      return res;
    } catch (Exception e) {
      log.error("Error: {}", e.getMessage());
      return null;
    }
  }
}
