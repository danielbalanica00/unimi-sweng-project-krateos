package com.simpolab.server_main.user_authentication.dao;

import com.simpolab.server_main.user_authentication.domain.AppUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserLoginDAS implements UserLoginDAO {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public AppUser findByUsername(String username) {
        String query = "SELECT * FROM user WHERE username = ?";

        try {
            return jdbcTemplate.queryForObject(query, (rs, rowNum) -> {
                return new AppUser(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                );
            }, username);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
