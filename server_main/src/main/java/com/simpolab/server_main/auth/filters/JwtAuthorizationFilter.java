package com.simpolab.server_main.auth.filters;

import static com.simpolab.server_main.auth.utils.JWTUtils.instance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simpolab.server_main.auth.domain.JWTDecodedToken;
import java.io.IOException;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    if (
      request.getServletPath().equals("/api/v1/login") ||
      request.getServletPath().equals("/api/v1/token/refresh")
    ) {
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
        decodedJWT.getAuthorities()
      );
      SecurityContextHolder.getContext().setAuthentication(userPassAuthToken);
    } catch (Exception e) {
      //      log.error("Error --->", e);
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);

      new ObjectMapper()
        .writeValue(response.getOutputStream(), Map.of("error_message", e.getMessage()));
      return;
    }
    filterChain.doFilter(request, response);
  }
}
