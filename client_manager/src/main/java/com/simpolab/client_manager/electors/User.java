package com.simpolab.client_manager.electors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class User {

  private String username;
  private String password;

  @JsonCreator
  public User(
    @JsonProperty("username") String username,
    @JsonProperty("password") String password
  ) {
    this.username = username;
    this.password = password;
  }
}
