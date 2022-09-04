package com.simpolab.client_elector.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Elector {

  private Integer id;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private String password;

  @JsonCreator
  public Elector(
    @JsonProperty("firstName") String firstName,
    @JsonProperty("lastName") String lastName,
    @JsonProperty("username") String username,
    @JsonProperty("email") String email,
    @JsonProperty("password") String password
  ) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    this.email = email;
    this.password = password;
  }

  @JsonCreator
  public Elector(
    @JsonProperty("firstName") String firstName,
    @JsonProperty("lastName") String lastName,
    @JsonProperty("username") String username,
    @JsonProperty("email") String email,
    @JsonProperty("id") int id
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
