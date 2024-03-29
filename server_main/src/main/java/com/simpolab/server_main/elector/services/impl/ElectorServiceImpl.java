package com.simpolab.server_main.elector.services.impl;

import com.simpolab.server_main.db.ElectorDAO;
import com.simpolab.server_main.elector.domain.Elector;
import com.simpolab.server_main.elector.services.ElectorService;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElectorServiceImpl implements ElectorService {

  private final ElectorDAO electorDAO;

  @Override
  public void newElector(Elector elector) {
    try {
      electorDAO.create(elector);
    } catch (SQLException e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public Elector getElector(long id) {
    var elector = electorDAO.get(id);
    return elector.isEmpty() ? null : elector.get();
  }

  @Override
  public List<Elector> getElectors() {
    return electorDAO.getAll();
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
    return electorDAO.getByUsername(username);
  }
}
