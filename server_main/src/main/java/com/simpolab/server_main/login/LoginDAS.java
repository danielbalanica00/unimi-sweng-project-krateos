package com.simpolab.server_main.login;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class LoginDAS implements LoginDAO {
    private final JdbcTemplate jdbcTemplate;

    public LoginDAS(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean login(String username, String password) {
        String query = "select username from elector where username = ? and password = ?";
        String res;

        try {
            res = jdbcTemplate.queryForObject(query, String.class, username, password);
        } catch (Exception e) {
            res = "";
            System.out.println(e);
        }

        return res != "";
    }
}
