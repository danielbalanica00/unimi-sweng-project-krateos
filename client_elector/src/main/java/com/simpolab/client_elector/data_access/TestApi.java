package com.simpolab.client_elector.data_access;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;

public class TestApi {

  public static boolean sendPost(String url, String username, String password) {
    //        String url = "http://127.0.0.1:8080/api/v1/elector/auth";
    // setup req
    HttpPost req = new HttpPost(url);

    //setup url params (x-www-form-params)
    List<NameValuePair> urlParams = new ArrayList<NameValuePair>();
    urlParams.add(new BasicNameValuePair("username", username));
    urlParams.add(new BasicNameValuePair("password", stringToMD5(password)));

    System.out.println("username: " + username);
    System.out.println("password: " + stringToMD5(password));

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
    return Boolean.parseBoolean(result);
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
