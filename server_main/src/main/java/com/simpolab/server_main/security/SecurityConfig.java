package com.simpolab.server_main.security;

import com.simpolab.server_main.security.dao.UserLoginDAO;
import com.simpolab.server_main.security.domain.User;
import com.simpolab.server_main.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserLoginDAO userLoginDAO;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(username -> {
            User user = userLoginDAO.findByUsername(username);
            if (user == null) {
                log.error("User {} not found", username);
                throw new UsernameNotFoundException("User not found in the database");
            } else {
                log.info("User {} was found in the database", username);
            }

            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
        });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Replace default authentication URL
        JwtAuthenticationFilter authenticationFilter = new JwtAuthenticationFilter(authenticationManagerBean());
        authenticationFilter.setFilterProcessesUrl("/api/v1/login");

        //Disable Cross Site Request Forgery
        http.csrf().disable();

        //Set session management to Stateless (AKA disable session based authentication)
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //Set unauthorized requests handler
        http.exceptionHandling().authenticationEntryPoint(((request, response, exception) -> {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage());
        }));

        //Set permissions on endpoints
        http.authorizeRequests()
                //Public APIs
                .antMatchers("/api/v1/login", "/api/v1/token/refresh").permitAll()
                //Private APIs
                .anyRequest().authenticated();

        //Add filter to the chain
        http.addFilter(authenticationFilter);
    }

    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return authenticationManager();
    }
}


