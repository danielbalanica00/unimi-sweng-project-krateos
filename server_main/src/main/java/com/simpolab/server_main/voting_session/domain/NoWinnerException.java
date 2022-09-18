package com.simpolab.server_main.voting_session.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class NoWinnerException extends Exception {

  public static final int BALLOTTAGGIO = 0;
  public static final int BALLOTTAGGIO_CATEGORICO_PREFERENZE = 1;
  public static final int QUORUM_NOT_REACHED = 2;
  public static final int NO_ABSOLUTE_MAJORITY = 3;

  private final int code;
  private Long winningTopOption;
}
