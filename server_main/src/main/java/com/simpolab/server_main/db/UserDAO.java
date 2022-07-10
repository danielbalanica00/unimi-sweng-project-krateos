package com.simpolab.server_main.db;

import com.simpolab.server_main.user_authentication.domain.AppUser;
import java.sql.SQLException;

public interface UserDAO {
  AppUser findByUsername(String username);

  void removeByUsername(String username);

  void removeById(Long id);

  void newUser(AppUser user) throws SQLException;
}
