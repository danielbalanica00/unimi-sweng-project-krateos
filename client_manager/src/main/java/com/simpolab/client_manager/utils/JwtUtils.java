package com.simpolab.client_manager.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class JwtUtils {

  private JwtUtils() {}

  public static String[] getRoles(@NonNull String token) {
    //decode JWT token
    log.debug("Token: " + token);
    DecodedJWT decodedJWT = JWT.decode(token);

    //get claims(AKA roles) from the payload
    return decodedJWT.getClaim("roles").asArray(String.class);
  }
}
