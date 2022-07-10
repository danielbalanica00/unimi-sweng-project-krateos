package com.simpolab.server_main.user_authentication.services;

import com.simpolab.server_main.db.UserDAO;
import com.simpolab.server_main.user_authentication.domain.AppUser;
import java.util.Collection;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

  @Autowired
  private UserDAO userDAO;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    AppUser appUser = userDAO.findByUsername(username);
    if (appUser == null) {
      log.error("User {} not found", username);
      throw new UsernameNotFoundException("User not found in the database");
    } else {
      log.info("User {} was found in the database", username);
    }
    Collection<SimpleGrantedAuthority> authorities = List.of(
      new SimpleGrantedAuthority(appUser.getRole())
    );

    log.info("User: {}", appUser);
    return new User(appUser.getUsername(), appUser.getPassword(), authorities);
  }
  //  private Collection<? extends GrantedAuthority> getAuthorities(
  //      Collection<Role> roles) {
  //    List<GrantedAuthority> authorities
  //        = new ArrayList<>();
  //    for (Role role : roles) {
  //      authorities.add(new SimpleGrantedAuthority(role.getName()));
  //      role.getPrivileges().stream()
  //          .map(p -> new SimpleGrantedAuthority(p.getName()))
  //          .forEach(authorities::add);
  //    }
  //
  //    return authorities;
  //  }
}
