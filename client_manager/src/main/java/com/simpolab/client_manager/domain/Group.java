package com.simpolab.client_manager.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode(of = "id")
public class Group {

  private int id;
  private String name;

  @JsonCreator
  public Group(@JsonProperty("id") int id, @JsonProperty("name") String name) {
    this.id = id;
    this.name = name;
  }

  @JsonCreator
  public Group(@JsonProperty("name") String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name + "(" + id + ")";
  }
}
