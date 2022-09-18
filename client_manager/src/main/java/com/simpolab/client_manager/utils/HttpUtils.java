package com.simpolab.client_manager.utils;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.jetbrains.annotations.Nullable;

@Slf4j
public final class HttpUtils {

  public record Reponse(String body, int code) {}

  private HttpUtils() {}

  private static final String baseUrl = "http://127.0.0.1:8080";
  private static String accessToken;

  public static void setAuthHeader(String accessToken) {
    HttpUtils.accessToken = accessToken;
  }

  public static String get(String path) {
    return get(path, null);
  }

  public static String get(String path, @Nullable Map<String, String> headers) {
    var request = new HttpGet(baseUrl + path);

    if (headers != null) headers.forEach(request::setHeader);

    return makeRequest(request);
  }

  public static Reponse getWithCode(String path, @Nullable Map<String, String> headers) {
    var request = new HttpGet(baseUrl + path);

    if (headers != null) headers.forEach(request::setHeader);

    return makeRequestWithCode(request);
  }

  public static void delete(String path) {
    delete(path, null);
  }

  public static void delete(String path, @Nullable Map<String, String> headers) {
    var request = new HttpDelete(baseUrl + path);

    if (headers != null) headers.forEach(request::setHeader);

    makeRequest(request);
  }

  public static <T> String put(String path, T body) {
    return put(path, null, body);
  }

  public static <T> String put(String path, @Nullable Map<String, String> headers, T body) {
    var request = new HttpPut(baseUrl + path);

    if (headers != null) headers.forEach(request::setHeader);

    if (body != null) {
      request.setHeader("Content-type", "application/json");
      request.setEntity(new StringEntity(new Gson().toJson(body)));
    }

    return makeRequest(request);
  }

  public static <T> String patch(String path) {
    return patch(path, null, null);
  }

  public static <T> String patch(String path, T body) {
    return put(path, null, body);
  }

  public static <T> String patch(String path, @Nullable Map<String, String> headers, T body) {
    var request = new HttpPatch(baseUrl + path);

    if (headers != null) headers.forEach(request::setHeader);

    if (body != null) {
      request.setHeader("Content-type", "application/json");
      request.setEntity(new StringEntity(new Gson().toJson(body)));
    }

    return makeRequest(request);
  }

  public static String postUrlParams(String path, Map<String, String> params) {
    return postUrlParams(path, null, params);
  }

  public static String postUrlParams(
    String path,
    @Nullable Map<String, String> headers,
    Map<String, String> params
  ) {
    var request = new HttpPost(baseUrl + path);

    request.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
    if (headers != null) headers.forEach(request::setHeader);

    request.setEntity(new UrlEncodedFormEntity(formatUrlParams(params)));
    return makeRequest(request);
  }

  public static <T> String postJson(String path, T body) {
    return postJson(path, null, body);
  }

  public static <T> String postJson(String path, @Nullable Map<String, String> headers, T body) {
    var request = new HttpPost(baseUrl + path);

    request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
    if (headers != null) headers.forEach(request::setHeader);

    request.setEntity(new StringEntity(new Gson().toJson(body)));
    return makeRequest(request);
  }

  private static List<NameValuePair> formatUrlParams(Map<String, String> params) {
    List<NameValuePair> urlParams = new ArrayList<>();

    params.forEach((k, v) -> urlParams.add(new BasicNameValuePair(k, v)));

    return urlParams;
  }

  private static String makeRequest(ClassicHttpRequest request) {
    try {
      if (
        request.getHeader(HttpHeaders.AUTHORIZATION) == null && accessToken != null
      ) request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
    } catch (ProtocolException ignore) {}

    try (
      CloseableHttpClient httpClient = HttpClients.createDefault();
      CloseableHttpResponse res = httpClient.execute(request)
    ) {
      var result = EntityUtils.toString(res.getEntity());
      log.debug(
        "{} {} - Response [{}] {}",
        request.getMethod(),
        request.getPath(),
        res.getCode(),
        result.isBlank() ? "<empty>" : result
      );
      return result;
    } catch (ParseException e) {
      log.error("[Http Request] - Parsing Error", e);
      throw new IllegalStateException("Parsing Error", e);
    } catch (IOException e) {
      log.error("[Http Request] - Network Error", e);
      throw new IllegalStateException("Network Error", e);
    }
  }

  private static Reponse makeRequestWithCode(ClassicHttpRequest request) {
    try {
      if (
        request.getHeader(HttpHeaders.AUTHORIZATION) == null && accessToken != null
      ) request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
    } catch (ProtocolException ignore) {}

    try (
      CloseableHttpClient httpClient = HttpClients.createDefault();
      CloseableHttpResponse res = httpClient.execute(request)
    ) {
      var result = EntityUtils.toString(res.getEntity());
      log.debug(
        "{} {} - Response [{}] {}",
        request.getMethod(),
        request.getPath(),
        res.getCode(),
        result.isBlank() ? "<empty>" : result
      );

      return new Reponse(result, res.getCode());
    } catch (ParseException e) {
      log.error("[Http Request] - Parsing Error", e);
      throw new IllegalStateException("Parsing Error", e);
    } catch (IOException e) {
      log.error("[Http Request] - Network Error", e);
      throw new IllegalStateException("Network Error", e);
    }
  }
}
