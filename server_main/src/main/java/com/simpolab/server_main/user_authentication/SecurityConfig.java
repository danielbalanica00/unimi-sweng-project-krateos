package com.simpolab.server_main.user_authentication;

import com.simpolab.server_main.user_authentication.filters.JwtAuthenticationFilter;
import com.simpolab.server_main.user_authentication.filters.JwtAuthorizationFilter;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserDetailsService userDetailsService;
  private final PasswordEncoder bCryptPasswordEncoder;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
  }

  /**
   * Authorization Configuration
   *
   * @param http
   * @throws Exception
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    //Replace default authentication URL
    val authFilter = new JwtAuthenticationFilter(authenticationManagerBean());
    authFilter.setFilterProcessesUrl("/api/v1/login");

    //Disable Cross Site Request Forgery
    http.csrf().disable();

    //Set session management to Stateless (AKA disable session based authentication)
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    //Set unauthorized requests handler
    http
      .exceptionHandling()
      .authenticationEntryPoint(
        (
          (request, response, exception) -> {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage());
          }
        )
      );

    //Set permissions on endpoints
    http
      .authorizeRequests()
      //Public APIs
      .antMatchers("/api/v1/login", "/api/v1/token/refresh")
      .permitAll()
      //Private APIs
      .anyRequest()
      .authenticated();

    //Add filters to the chain
    http.addFilter(authFilter);
    http.addFilterBefore(new JwtAuthorizationFilter(), JwtAuthenticationFilter.class);
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return authenticationManager();
  }
}
