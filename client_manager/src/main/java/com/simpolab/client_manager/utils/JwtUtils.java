package com.simpolab.client_manager.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.NonNull;

public class JwtUtils {
  public static String[] getRoles(@NonNull String token) {
    //decode JWT token
    System.out.println("Token: " + token);
    DecodedJWT decodedJWT = JWT.decode(token);

    //get claims(AKA roles) from the payload
    return decodedJWT.getClaim("roles").asArray(String.class);
  }
}
