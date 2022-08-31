package com.simpolab.client_manager.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import lombok.NonNull;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Api {

  public static String token = "";
  private static final String baseUrl = "http://127.0.0.1:8080";


  public static String postUrlParams(String path, Map<String, String> params) {
    return postUrlParams(path, null, params);
  }

  public static String postUrlParams(String path, @Nullable Map<String, String> headers, Map<String, String> params) {
    var request = new HttpPost(baseUrl + path);

    request.setHeader("Content-type", "application/x-www-form-urlencoded");
    if (headers != null) headers.forEach(request::setHeader);


    request.setEntity(new UrlEncodedFormEntity(formatUrlParams(params)));
    return makeRequest(request);
  }

  public static <T> String postJson(String path, T body) {
    return postJson(path, null, body);
  }

  public static <T> String postJson(String path, @Nullable Map<String, String> headers, T body) {
    var request = new HttpPost(baseUrl + path);

    request.setHeader("Content-type", "application/json");
    if (headers != null) headers.forEach(request::setHeader);

    request.setEntity(new StringEntity(new Gson().toJson(body)));
    return makeRequest(request);
  }

  private static String makeRequest(ClassicHttpRequest request) {
    try (CloseableHttpClient httpClient = HttpClients.createDefault(); CloseableHttpResponse res = httpClient.execute(request)) {
      var result = EntityUtils.toString(res.getEntity());
      System.out.println("RES: " + result);
      return result;
    } catch (Exception e) {
      System.out.println("******** ERRORE *********");
      e.printStackTrace();
    }
    throw new IllegalStateException("Shouldn't be here");
  }

  private static List<NameValuePair> formatUrlParams(Map<String, String> params) {
    List<NameValuePair> urlParams = new ArrayList<>();

    params.forEach((k, v) -> urlParams.add(new BasicNameValuePair(k, v)));

    return urlParams;
  }


  public static String sendGet(String url, String token) {
    HttpGet req = new HttpGet(url);
    System.out.println(req.toString());

    req.addHeader("Authorization", "Bearer " + token);
    System.out.println(Arrays.toString(req.getHeaders()));

    String result = "";
    try (CloseableHttpClient httpClient = HttpClients.createDefault(); CloseableHttpResponse res = httpClient.execute(req)) {
      result = EntityUtils.toString(res.getEntity());
    } catch (Exception e) {
      System.out.println("******** ERRORE *********");
      e.printStackTrace();
    }

    System.out.println("RES: " + result);
    return result;
  }

  public static String get(String path, @Nullable Map<String, String> headers) {
    var request = new HttpGet(baseUrl + path);

    if (headers != null) headers.forEach(request::setHeader);

    return makeRequest(request);
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
