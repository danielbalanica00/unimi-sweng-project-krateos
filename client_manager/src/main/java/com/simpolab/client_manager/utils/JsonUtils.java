package com.simpolab.client_manager.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;


public final class JsonUtils {
  private JsonUtils() {}

  public static <T> T parseJson(String jsonString, Class<T> objectClass) {
    return new Gson().fromJson(jsonString, objectClass);
  }

  public static <T> List<T> parseJsonArray(String jsonString, Class<T> objectClass) {
    var type = TypeToken.getParameterized(List.class, objectClass).getType();
    return new Gson().fromJson(jsonString, type);
  }
}
