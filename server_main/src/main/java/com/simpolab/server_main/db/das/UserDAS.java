package com.simpolab.server_main.db.das;

import com.simpolab.server_main.auth.domain.AppUser;
import com.simpolab.server_main.db.UserDAO;
import java.sql.SQLException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserDAS implements UserDAO {

  private final JdbcTemplate jdbcTemplate;
  private final PasswordEncoder passwordEncoder;

  @Override
  public AppUser findByUsername(String username) {
    String query = "SELECT * FROM user WHERE username = ?";

    try {
      return jdbcTemplate.queryForObject(
        query,
        (rs, rowNum) ->
          new AppUser(
            rs.getLong("id"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("role")
          ),
        username
      );
    } catch (Exception e) {
      log.error(e.getMessage());
      return null;
    }
  }

  @Override
  public Optional<AppUser> findByUsername2(String username) {
    String query = "SELECT * FROM user WHERE username = ?";

    try {
      AppUser user = jdbcTemplate.queryForObject(
        query,
        (rs, rowNum) ->
          new AppUser(
            rs.getLong("id"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("role")
          ),
        username
      );

      return Optional.ofNullable(user);
    } catch (Exception e) {
      log.error(e.getMessage());
      return Optional.empty();
    }
  }

  @Override
  public void removeByUsername(String username) {
    String removeUser = "REMOVE FROM user where username = ?";
    remove(removeUser, username);
  }

  @Override
  public void removeById(Long id) {
    String removeUser = "REMOVE FROM user where id = ?";
    remove(removeUser, id);
  }

  private void remove(String query, Object parameter) {
    try {
      jdbcTemplate.update(query, parameter);
      log.info("User {} removed successfully", parameter);
    } catch (Exception e) {
      log.error("Failed to remove user", e);
    }
  }

  @Override
  public void newUser(AppUser user) throws SQLException {
    try {
      String createUser = "INSERT INTO user (username, password, role) VALUES (?, ?, ?)";
      jdbcTemplate.update(
        createUser,
        user.getUsername(),
        passwordEncoder.encode(user.getPassword()),
        user.getRole()
      );
      log.debug("User created successfully");
    } catch (DuplicateKeyException e) {
      log.debug("The user with username {} already exists, skipping creation", user.getUsername());
    } catch (Exception e) {
      log.error("Failed to create new user", e);
      throw new SQLException("Failed to create new user");
    }
  }
}
