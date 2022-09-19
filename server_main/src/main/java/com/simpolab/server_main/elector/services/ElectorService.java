package com.simpolab.server_main.elector.services;

import com.simpolab.server_main.elector.domain.NewElector;
import java.util.List;
import java.util.Optional;

public interface ElectorService {
  void newElector(NewElector newElector);

  NewElector getElector(long id);

  List<NewElector> getElectors();

  void deleteElector(Long id);

  Optional<NewElector> getElectorByUsername(String username);
}
