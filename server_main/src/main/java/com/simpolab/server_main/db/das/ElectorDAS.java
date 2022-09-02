package com.simpolab.server_main.db.das;

import com.simpolab.server_main.db.ElectorDAO;
import com.simpolab.server_main.db.UserDAO;
import com.simpolab.server_main.elector.domain.Elector;
import com.simpolab.server_main.elector.domain.NewElector;
import com.simpolab.server_main.user_authentication.domain.AppUser;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ElectorDAS implements ElectorDAO {

  private final JdbcTemplate jdbcTemplate;

  private final RowMapper<Elector> electorRowMapper = (rs, rowNum) -> {
    var appUser = new AppUser(
      rs.getLong("id"),
      rs.getString("username"),
      rs.getString("password"),
      rs.getString("role")
    );

    return new Elector(
      appUser,
      rs.getString("first_name"),
      rs.getString("last_name"),
      rs.getString("email")
    );
  };

  private final RowMapper<NewElector> newElectorRowMapper = (rs, rowNum) ->
    NewElector
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
  public void create(NewElector newElector) throws SQLException {
    newElector.setRole("elector");

    try {
      userRepo.newUser(AppUser.from(newElector));
      Optional<AppUser> user = userRepo.findByUsername2(newElector.getUsername());
      if (user.isEmpty()) throw new IllegalStateException(
        "[XXX] - Couldn't find the user that was just created"
      );

      String createElector =
        "INSERT IGNORE INTO elector (id, email, first_name, last_name) VALUES (?, ?, ?, ?)";
      var rows = jdbcTemplate.update(
        createElector,
        user.get().getId(),
        newElector.getEmail(),
        newElector.getFirstName(),
        newElector.getLastName()
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
      log.info("Elector {} removed successfully", id);
    } catch (Exception e) {
      log.error("Failed to remove user", e);
    }
  }

  @Override
  public Optional<NewElector> get(long id) {
    String query = "SELECT * FROM elector JOIN user WHERE elector.id = user.id AND elector.id = ?";
    try {
      NewElector newElector = jdbcTemplate.queryForObject(query, newElectorRowMapper, id);
      return Optional.ofNullable(newElector);
    } catch (Exception e) {
      log.error(e.getMessage());
      return Optional.empty();
    }
  }

  @Override
  public Elector getByUsername(String username) {
    String query =
      "SELECT * FROM elector JOIN user WHERE elector.id = user.id AND user.username = ?";
    try {
      return jdbcTemplate.queryForObject(query, electorRowMapper, username);
    } catch (Exception e) {
      log.warn(e.getMessage());
      return null;
    }
  }

  @Override
  public List<NewElector> getAll() {
    String query = "SELECT * FROM elector JOIN user WHERE elector.id = user.id";
    try {
      return jdbcTemplate.query(query, newElectorRowMapper);
    } catch (Exception e) {
      log.warn(e.getMessage());
      return List.of();
    }
  }

  @Override
  public List<NewElector> getAllInGroup(Long groupId) {
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
