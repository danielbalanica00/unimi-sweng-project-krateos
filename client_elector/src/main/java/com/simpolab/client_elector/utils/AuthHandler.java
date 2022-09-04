package com.simpolab.client_elector.utils;

import com.simpolab.client_elector.domain.AuthTokens;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class AuthHandler {

  private AuthHandler() {}

  private static AuthTokens authTokens;

  private static final String loginPath = "/api/v1/login";

  public static int login(String username, String password) {
    var credentials = Map.of("username", username, "password", password);

    String tokensJson = HttpUtils.postUrlParams(loginPath, credentials);

    // CASE 1: Failed login
    if (tokensJson.isBlank()) return 0;

    // Extract auth tokens
    AuthTokens authTokens = JsonUtils.parseJson(tokensJson, AuthTokens.class);
    String[] roles = JwtUtils.getRoles(authTokens.getAccessToken());

    // CASE 2: Login successfully but user is not a manager
    if (!roles[0].equals("ELECTOR")) return 1;

    // CASE 3: Login successfully and user is a manager
    AuthHandler.authTokens = authTokens;
    HttpUtils.setAuthHeader(authTokens.getAccessToken());
    return 2;
  }

  public static String getAccessToken() {
    return authTokens.getAccessToken();
  }
}
