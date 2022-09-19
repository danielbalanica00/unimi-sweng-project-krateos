package com.simpolab.server_main.auth.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simpolab.server_main.auth.domain.JWTTokens;
import com.simpolab.server_main.auth.utils.JWTUtils;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  /**
   * This class replaces spring's UserPasswordAuthenticationFilter in the filter chain
   */

  private final AuthenticationManager authenticationManager;

  @Override
  public Authentication attemptAuthentication(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws AuthenticationException {
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    log.info("Username is: {}", username);
    log.info("Password is: {}", password);

    val authToken = new UsernamePasswordAuthenticationToken(username, password);

    //passo all'autthentication manager le credenziali fornite dall'utente in modo che le confronti con quelle legate all'utername dato
    return authenticationManager.authenticate(authToken);
  }

  @Override
  protected void successfulAuthentication(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain chain,
    Authentication authResult
  ) throws IOException {
    User user = (User) authResult.getPrincipal();

    JWTTokens tokens = JWTUtils.instance.createTokens(
      user.getUsername(),
      request.getRequestURL().toString(),
      user.getAuthorities()
    );

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    new ObjectMapper().writeValue(response.getOutputStream(), tokens);
  }
}
