package com.simpolab.server_main.dao;

import com.simpolab.server_main.elector.domain.Elector;
import java.sql.SQLException;

public interface ElectorDAO {
  void newElector(Elector newElector) throws SQLException;

  Elector getElectorById(Long id);
}
