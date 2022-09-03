package com.simpolab.client_manager.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthTokens {

  private String accessToken;
  private String refreshToken;
}
