package com.simpolab.server_main.dao;

import com.simpolab.server_main.elector.domain.Elector;
import com.simpolab.server_main.user_authentication.domain.AppUser;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ElectorDAS implements ElectorDAO {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  private UserDAO userRepo;

  @Override
  public void newElector(Elector newElector) throws SQLException {
    Long id = null;
    var appUser = newElector.getUser();
    appUser.setRole("elector");

    try {
      userRepo.newUser(appUser);
      id = userRepo.findByUsername(appUser.getUsername()).getId();
      if (id == null) throw new IllegalStateException("The user with id " + id + " was not found");

      String createElector =
        "INSERT IGNORE INTO elector (id, email, first_name, last_name) VALUES (?, ?, ?, ?)";
      jdbcTemplate.update(
        createElector,
        id,
        newElector.getEmail(),
        newElector.getFirstName(),
        newElector.getLastName()
      );
    } catch (Exception e) {
      log.error("Failed to create new elector");
      throw new SQLException("Failed to create new elector", e);
    }
    log.info("Elector created successfully");
  }

  @Override
  public Elector getElectorById(Long id) {
    String query = "SELECT * FROM elector JOIN user WHERE elector.id = user.id AND elector.id = ?";
    try {
      return jdbcTemplate.queryForObject(
        query,
        (rs, rowNum) -> {
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
        },
        id
      );
    } catch (Exception e) {
      log.warn(e.getMessage());
      return null;
    }
  }
}
