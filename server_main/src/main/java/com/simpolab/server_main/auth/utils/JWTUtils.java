package com.simpolab.server_main.auth.utils;

import static java.util.Arrays.stream;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.simpolab.server_main.auth.domain.JWTDecodedToken;
import com.simpolab.server_main.auth.domain.JWTTokens;
import java.util.Collection;
import java.util.Date;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
// https://www.baeldung.com/spring-boot-yaml-vs-properties
public class JWTUtils {

  private static Algorithm algorithm;

  public static JWTUtils instance = new JWTUtils();

  private JWTUtils() {
    algorithm = Algorithm.HMAC256("This is a secret");
  }

  public JWTTokens createTokens(
    @NonNull String username,
    @NonNull String issuer,
    @NonNull Collection<GrantedAuthority> authorities
  ) {
    String accessToken = JWT
      .create()
      .withSubject(username)
      .withExpiresAt(new Date(System.currentTimeMillis() + 20 * 60 * 1000)) //20 mins
      .withIssuer(issuer)
      .withClaim("roles", authorities.stream().map(GrantedAuthority::getAuthority).toList())
      .sign(algorithm);

    String refreshToken = JWT
      .create()
      .withSubject(username)
      .withExpiresAt(new Date(System.currentTimeMillis() + 40 * 60 * 1000)) //40 mins
      .withIssuer(issuer)
      .withClaim("roles", authorities.stream().map(GrantedAuthority::getAuthority).toList())
      .sign(algorithm);

    return new JWTTokens(accessToken, refreshToken);
  }

  public JWTDecodedToken verifyToken(@NonNull String token) {
    //decode JWT token
    DecodedJWT decodedJWT = JWT.require(algorithm).build().verify(token);

    //get claims from the payload
    String username = decodedJWT.getSubject();
    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
    Collection<SimpleGrantedAuthority> authorities = stream(roles)
      .map(SimpleGrantedAuthority::new)
      .toList();

    return new JWTDecodedToken(username, authorities);
  }
}
