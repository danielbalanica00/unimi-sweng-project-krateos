package com.simpolab.client_manager.login;

import com.simpolab.client_manager.utils.HttpUtils;
import com.simpolab.client_manager.utils.JsonUtils;
import com.simpolab.client_manager.utils.JwtUtils;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
public class LoginSession {
  private static AuthTokens authTokens;

  private static final String loginPath = "/api/v1/login";

  public static int login(String username, String password) {
    var credentials = Map.of("username", username, "password", password);


    String tokensJson = HttpUtils.postUrlParams(loginPath, credentials);
    System.out.println("Got: " + tokensJson);


    // CASE 1: Failed login
    if (tokensJson == null || tokensJson.isBlank()) return 0;

    // Extract auth tokens
    AuthTokens authTokens = JsonUtils.parseJson(tokensJson, AuthTokens.class);
    String[] roles = JwtUtils.getRoles(authTokens.getAccessToken());

    // CASE 2: Login successfully but user is not a manager
    if (!roles[0].equals("MANAGER")) return 1;

    // CASE 3: Login successfully and user is a manager
    LoginSession.authTokens = authTokens;
    return 2;
  }

  public static String getAccessToken(){
    return authTokens.getAccessToken();
  }
}
