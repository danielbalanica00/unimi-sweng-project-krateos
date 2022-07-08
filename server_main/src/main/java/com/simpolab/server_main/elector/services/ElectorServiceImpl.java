package com.simpolab.server_main.elector.services;

import com.simpolab.server_main.dao.ElectorDAO;
import com.simpolab.server_main.elector.domain.Elector;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ElectorServiceImpl implements ElectorService {

  @Autowired
  private ElectorDAO electorDAO;

  @Override
  public void createNewElector(Elector newElector) {
    try {
      electorDAO.newElector(newElector);
    } catch (SQLException e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public Elector getElectorById(Long id) {
    var elector = electorDAO.getElectorById(id);
    if (elector == null) return null;

    var user = elector.getUser();
    user.setPassword(null);
    user.setId(null);
    user.setRole(null);
    return elector;
  }
}
