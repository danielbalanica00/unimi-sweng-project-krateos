package com.simpolab.client_manager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Launcher {

  public static void main(String[] args) {
    log.debug("Launching application");
    App.main(args);
  }
}
