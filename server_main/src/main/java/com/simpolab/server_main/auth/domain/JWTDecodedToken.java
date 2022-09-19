package com.simpolab.server_main.auth.domain;

import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
@AllArgsConstructor
public class JWTDecodedToken {

  private final String username;
  private final Collection<SimpleGrantedAuthority> authorities;
}
