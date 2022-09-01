package com.simpolab.client_manager.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.NonNull;

import java.util.Arrays;

public class JwtUtils {
  public static void verifyToken(@NonNull String token) {
    //decode JWT token
    System.out.println("Token: " + token);
    DecodedJWT decodedJWT = JWT.decode(token);

    //get claims from the payload
    String username = decodedJWT.getSubject();
    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);


    System.out.println("Username: " + username + " roles: " + Arrays.toString(roles));
  }
}
