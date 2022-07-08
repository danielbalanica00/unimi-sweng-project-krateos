package com.simpolab.server_main.elector.services;

import com.simpolab.server_main.elector.domain.Elector;

public interface ElectorService {
  void createNewElector(Elector newElector);

  Elector getElectorById(Long id);
}
