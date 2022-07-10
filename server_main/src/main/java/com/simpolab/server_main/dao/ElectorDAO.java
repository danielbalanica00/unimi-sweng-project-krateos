package com.simpolab.server_main.dao;

import com.simpolab.server_main.elector.domain.Elector;
import java.sql.SQLException;
import java.util.List;

public interface ElectorDAO {
  void create(Elector newElector) throws SQLException;

  void delete(Long id) throws SQLException;

  Elector get(Long id);

  List<Elector> getAll();
}
