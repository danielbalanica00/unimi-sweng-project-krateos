package com.simpolab.server_main.users.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import static java.util.Arrays.stream;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/api/v1/login") || request.getServletPath().equals("/api/v1/token/refresh")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            //decode JWT token
            String token = authHeader.substring("Bearer ".length());
            Algorithm alg = Algorithm.HMAC256("This is a secret");
            JWTVerifier verifier = JWT.require(alg).build();
            DecodedJWT decodedJWT = verifier.verify(token);

            //get claims from the payload
            String username = decodedJWT.getSubject();
            String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
            Collection<SimpleGrantedAuthority> authorities = stream(roles).map(SimpleGrantedAuthority::new).toList();

            //set the security context to the given user
            val userPassAuthToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(userPassAuthToken);

            filterChain.doFilter(request, response);
            return;
        } catch (Exception e) {
            log.error("Error logging in: {}", e.getMessage());

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            Map<String, String> err = Map.of("error_message", e.getMessage());

            new ObjectMapper().writeValue(response.getOutputStream(), err);
        }
    }
}
