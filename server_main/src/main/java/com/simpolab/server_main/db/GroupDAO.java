package com.simpolab.server_main.db;

import com.simpolab.server_main.group.domain.Group;
import java.sql.SQLException;
import java.util.List;

public interface GroupDAO {
  void create(String name) throws SQLException;

  Group get(Long id);

  List<Group> getAll();

  void delete(Long id);

  void addElector(Long groupId, Long electorId) throws SQLException;

  void removeElector(Long groupId, Long electorId);
}
