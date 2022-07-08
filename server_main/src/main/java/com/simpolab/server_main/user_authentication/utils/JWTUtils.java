package com.simpolab.server_main.user_authentication.utils;

import static java.util.Arrays.stream;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Collection;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

// https://www.baeldung.com/spring-boot-yaml-vs-properties
public class JWTUtils {

  @Value("${auth.secret}")
  private static String secret;

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
      .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000)) //10 mins
      .withIssuer(issuer)
      .withClaim("roles", authorities.stream().map(GrantedAuthority::getAuthority).toList())
      .sign(algorithm);

    String refreshToken = JWT
      .create()
      .withSubject(username)
      .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000)) //30 mins
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

  @Getter
  @AllArgsConstructor
  public static class JWTDecodedToken {

    private final String username;
    private final Collection<SimpleGrantedAuthority> authorities;
  }

  @Getter
  @AllArgsConstructor
  public static class JWTTokens {

    private final String accessToken;
    private final String refreshToken;
  }
}
