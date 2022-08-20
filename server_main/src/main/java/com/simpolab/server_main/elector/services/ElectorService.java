package com.simpolab.server_main.elector.services;

import com.simpolab.server_main.elector.domain.Elector;
import java.util.List;
import java.util.Optional;

public interface ElectorService {
  void newElector(Elector newElector);

  Elector getElector(Long id);

  List<Elector> getElectors();

  void deleteElector(Long id);

  Optional<Elector> getElectorByUsername(String username);
}
