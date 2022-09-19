package com.simpolab.server_main.db.das;

import com.simpolab.server_main.auth.domain.AppUser;
import com.simpolab.server_main.db.ElectorDAO;
import com.simpolab.server_main.db.UserDAO;
import com.simpolab.server_main.elector.domain.Elector;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ElectorDAS implements ElectorDAO {

  private final JdbcTemplate jdbcTemplate;

  private final RowMapper<Elector> newElectorRowMapper = (rs, rowNum) ->
    Elector
      .builder()
      .id(rs.getLong("id"))
      .username(rs.getString("username"))
      .password(rs.getString("password"))
      .role(rs.getString("role"))
      .firstName(rs.getString("first_name"))
      .lastName(rs.getString("last_name"))
      .email(rs.getString("email"))
      .build();

  @Autowired
  private UserDAO userRepo;

  @Override
  public void create(Elector elector) throws SQLException {
    elector.setRole("elector");

    try {
      userRepo.newUser(AppUser.from(elector));
      Optional<AppUser> user = userRepo.findByUsername2(elector.getUsername());
      if (user.isEmpty()) throw new IllegalStateException(
        "[XXX] - Couldn't find the user that was just created"
      );

      String createElector =
        "INSERT IGNORE INTO elector (id, email, first_name, last_name) VALUES (?, ?, ?, ?)";
      var rows = jdbcTemplate.update(
        createElector,
        user.get().getId(),
        elector.getEmail(),
        elector.getFirstName(),
        elector.getLastName()
      );
      log.debug("affected rows: " + rows);
    } catch (Exception e) {
      log.error("Failed to create new elector");
      throw new SQLException("Failed to create new elector", e);
    }
    log.debug("Elector created successfully");
  }

  @Override
  public void delete(Long id) throws SQLException {
    var query = "DELETE FROM elector WHERE id = ?";
    try {
      jdbcTemplate.update(query, id);
      log.info("Elector {} deleted successfully", id);
    } catch (Exception e) {
      log.error("Failed to delete user " + id, e);
      throw new SQLException("Failed to delete user " + id, e);
    }
  }

  @Override
  public Optional<Elector> get(long id) {
    String query = "SELECT * FROM elector JOIN user WHERE elector.id = user.id AND elector.id = ?";
    try {
      Elector elector = jdbcTemplate.queryForObject(query, newElectorRowMapper, id);
      return Optional.ofNullable(elector);
    } catch (Exception e) {
      log.error(e.getMessage());
      return Optional.empty();
    }
  }

  @Override
  public Optional<Elector> getByUsername(String username) {
    String query =
      "SELECT * FROM elector JOIN user WHERE elector.id = user.id AND user.username = ?";
    try {
      val elector = jdbcTemplate.queryForObject(query, newElectorRowMapper, username);
      return Optional.ofNullable(elector);
    } catch (Exception e) {
      log.warn(e.getMessage());
      return Optional.empty();
    }
  }

  @Override
  public List<Elector> getAll() {
    String query = "SELECT * FROM elector JOIN user WHERE elector.id = user.id";
    try {
      return jdbcTemplate.query(query, newElectorRowMapper);
    } catch (Exception e) {
      log.warn(e.getMessage());
      return List.of();
    }
  }

  @Override
  public List<Elector> getAllInGroup(Long groupId) {
    String query =
      "SELECT * FROM elector_group AS eg, elector, user WHERE elector.id = user.id AND eg.elector_id = elector.id AND eg.voting_group_id = ?";
    try {
      return jdbcTemplate.query(query, newElectorRowMapper, groupId);
    } catch (Exception e) {
      log.warn(e.getMessage());
      return List.of();
    }
  }
}
