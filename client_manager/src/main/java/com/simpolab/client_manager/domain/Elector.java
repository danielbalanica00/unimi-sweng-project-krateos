package com.simpolab.client_manager.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Elector {

  private String id;
  private String username;
  private String firstName;
  private String lastName;
  private String email;

  @JsonCreator
  public Elector(
    @JsonProperty("firstName") String firstName,
    @JsonProperty("lastName") String lastName,
    @JsonProperty("username") String username,
    @JsonProperty("email") String email,
    @JsonProperty("id") String id
  ) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    this.email = email;
    this.id = id;
  }

  @Override
  public String toString() {
    return firstName + " " + lastName + "  (" + username + ")";
  }
}
