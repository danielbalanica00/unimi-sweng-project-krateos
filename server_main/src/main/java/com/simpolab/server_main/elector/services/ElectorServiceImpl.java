package com.simpolab.server_main.elector.services;

import com.simpolab.server_main.db.ElectorDAO;
import com.simpolab.server_main.elector.domain.Elector;
import com.simpolab.server_main.elector.domain.NewElector;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ElectorServiceImpl implements ElectorService {

  @Autowired
  private ElectorDAO electorDAO;

  @Override
  public void newElector(NewElector newElector) {
    try {
      electorDAO.create(newElector);
    } catch (SQLException e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public NewElector getElector(long id) {
    var elector = electorDAO.get(id);
    return elector.isEmpty() ? null : elector.get().sanitized();
  }

  @Override
  public List<NewElector> getElectors() {
    List<NewElector> electors = electorDAO.getAll();
    return electors.stream().map(NewElector::sanitized).toList();
  }

  @Override
  public void deleteElector(Long id) {
    try {
      electorDAO.delete(id);
    } catch (SQLException e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public Optional<Elector> getElectorByUsername(String username) {
    return Optional.ofNullable(electorDAO.getByUsername(username));
  }
}
