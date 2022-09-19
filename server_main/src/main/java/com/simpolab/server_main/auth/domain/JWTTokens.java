package com.simpolab.server_main.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JWTTokens {

  private final String accessToken;
  private final String refreshToken;
}
