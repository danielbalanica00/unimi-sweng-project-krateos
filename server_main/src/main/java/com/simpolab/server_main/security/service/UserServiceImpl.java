package com.simpolab.server_main.security.service;

import com.simpolab.server_main.security.dao.UserLoginDAO;
import com.simpolab.server_main.security.domain.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private UserLoginDAO userLoginDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userLoginDAO.findByUsername(username);
        if (appUser == null) {
            log.error("User {} not found", username);
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User {} was found in the database", username);
        }
        Collection<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(appUser.getRole()));
        System.out.println(new User(appUser.getUsername(), appUser.getPassword(), authorities));
        return new User(appUser.getUsername(), appUser.getPassword(), authorities);
    }
}
