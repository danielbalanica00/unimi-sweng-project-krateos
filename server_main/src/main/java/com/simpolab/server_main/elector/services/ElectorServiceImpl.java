package com.simpolab.server_main.elector.services;

import com.simpolab.server_main.dao.ElectorDAO;
import com.simpolab.server_main.elector.domain.Elector;
import java.sql.SQLException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ElectorServiceImpl implements ElectorService {

  @Autowired
  private ElectorDAO electorDAO;

  @Override
  public void newElector(Elector newElector) {
    try {
      electorDAO.create(newElector);
    } catch (SQLException e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public Elector getElector(Long id) {
    var elector = electorDAO.get(id);
    if (elector == null) return null;

    var user = elector.getUser();
    user.sanitize();

    return elector;
  }

  @Override
  public List<Elector> getElectors() {
    var electors = electorDAO.getAll();

    electors.forEach(elector -> elector.getUser().sanitize());

    return electors;
  }

  @Override
  public void deleteElector(Long id) {
    try {
      electorDAO.delete(id);
    } catch (SQLException e) {
      throw new IllegalArgumentException(e);
    }
  }
}
