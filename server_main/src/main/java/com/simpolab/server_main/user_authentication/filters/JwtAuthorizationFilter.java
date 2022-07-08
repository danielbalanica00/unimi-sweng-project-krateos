package com.simpolab.server_main.user_authentication.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static com.simpolab.server_main.user_authentication.utils.JWTUtils.JWTDecodedToken;
import static com.simpolab.server_main.user_authentication.utils.JWTUtils.instance;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    if (request.getServletPath().equals("/api/v1/login")
        || request.getServletPath().equals("/api/v1/token/refresh")) {
      filterChain.doFilter(request, response);
      return;
    }

    String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      String token = authHeader.substring("Bearer ".length());
      JWTDecodedToken decodedJWT = instance.verifyToken(token);

      //set the security context to the given user
      val userPassAuthToken = new UsernamePasswordAuthenticationToken(
          decodedJWT.getUsername(),
          null,
          decodedJWT.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(userPassAuthToken);

      filterChain.doFilter(request, response);
    } catch (Exception e) {
      log.error("Error logging in: {}", e.getMessage());
      e.printStackTrace();
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);

      new ObjectMapper().writeValue(response.getOutputStream(), Map.of("error_message", e.getMessage()));
    }
  }
}
