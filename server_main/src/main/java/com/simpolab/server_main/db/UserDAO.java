package com.simpolab.server_main.db;

import com.simpolab.server_main.auth.domain.AppUser;
import java.sql.SQLException;
import java.util.Optional;

public interface UserDAO {
  AppUser findByUsername(String username);

  Optional<AppUser> findByUsername2(String password);

  void removeByUsername(String username);

  void removeById(Long id);

  void newUser(AppUser user) throws SQLException;
}
