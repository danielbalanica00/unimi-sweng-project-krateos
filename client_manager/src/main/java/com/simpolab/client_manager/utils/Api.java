package com.simpolab.client_manager.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.NonNull;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Api {

    public static String token = "";

    public static String sendPost(String url, Map<String, String> params) {
        HttpPost req = new HttpPost(url);

        //setup url params (x-www-form-params)
        List<NameValuePair> urlParams = new ArrayList<NameValuePair>();

        for(Map.Entry<String, String> entry : params.entrySet()){
            urlParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        //add the url params to the req
        try {
            req.setEntity(new UrlEncodedFormEntity(urlParams));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //make the req and parse the result
        String result = "";
        try (
                CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse res = httpClient.execute(req)
        ) {
            result = EntityUtils.toString(res.getEntity());
        } catch (Exception e) {
            System.out.println("******** ERRORE *********");
            e.printStackTrace();
        }

        System.out.println("RES: " + result);
        return result;
    }

    public static String sendGet(String url, String token) {
        HttpGet req = new HttpGet(url);

        req.addHeader("Authorization", "Bearer " + token);

        String result = "";
        try (
                CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse res = httpClient.execute(req)
        ) {
            result = EntityUtils.toString(res.getEntity());
        } catch (Exception e) {
            System.out.println("******** ERRORE *********");
            e.printStackTrace();
        }

        System.out.println("RES: " + result);
        return result;
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

    public static void verifyToken(@NonNull String token) {
        //decode JWT token
        System.out.println("Token: " + token);
        DecodedJWT decodedJWT = JWT.decode(token);

        //get claims from the payload
        String username = decodedJWT.getSubject();
        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);


        System.out.println("Username: " + username + " roles: " + Arrays.toString(roles));
    }
}