package com.simpolab.server_main.security.dao;

import com.simpolab.server_main.security.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserLoginDAS implements UserLoginDAO {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User findByUsername(String username) {
        String query = "SELECT * FROM elector WHERE username = ?";

        try {
            return jdbcTemplate.queryForObject(query, (rs, rowNum) -> {
                User user = new User();

                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));

                return user;
            }, username);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
