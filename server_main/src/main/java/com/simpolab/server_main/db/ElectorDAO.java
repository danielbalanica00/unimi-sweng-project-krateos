package com.simpolab.server_main.db;

import com.simpolab.server_main.elector.domain.Elector;
import java.sql.SQLException;
import java.util.List;

public interface ElectorDAO {
  void create(Elector newElector) throws SQLException;

  void delete(Long id) throws SQLException;

  Elector get(Long id);

  Elector getByUsername(String username);

  List<Elector> getAll();

  List<Elector> getAllInGroup(Long groupId);
}
