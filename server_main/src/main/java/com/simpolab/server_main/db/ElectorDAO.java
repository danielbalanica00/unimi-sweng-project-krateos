package com.simpolab.server_main.db;

import com.simpolab.server_main.elector.domain.Elector;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ElectorDAO {
  void create(Elector elector) throws SQLException;

  void delete(Long id) throws SQLException;

  Optional<Elector> get(long id);

  Optional<Elector> getByUsername(String username);

  List<Elector> getAll();

  List<Elector> getAllInGroup(Long groupId);
}
