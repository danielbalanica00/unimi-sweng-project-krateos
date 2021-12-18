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
            res = jdbcTemplate.queryForObject(query, String.class, username, stringToMD5(password));
        } catch (Exception e) {
            res = "";
            System.out.println(e);
        }

        return res != "";
    }

    private static String stringToMD5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(str.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);

            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
