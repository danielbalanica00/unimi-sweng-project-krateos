package com.simpolab.server_main.db;

import com.simpolab.server_main.elector.domain.Elector;
import com.simpolab.server_main.elector.domain.NewElector;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ElectorDAO {
  void create(NewElector newElector) throws SQLException;

  void delete(Long id) throws SQLException;

  Optional<NewElector> get(long id);

  Elector getByUsername(String username);

  List<NewElector> getAll();

  List<NewElector> getAllInGroup(Long groupId);
}
