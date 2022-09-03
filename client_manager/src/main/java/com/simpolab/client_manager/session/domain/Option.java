package com.simpolab.client_manager.session.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Option {

  private Integer id;
  private String value;
  private Integer parentId;

  @JsonCreator
  public Option(@JsonProperty("value") String value) {
    this.value = value;
  }

  @JsonCreator
  public Option(@JsonProperty("id") int id, @JsonProperty("value") String value) {
    this.id = id;
    this.value = value;
  }

  @JsonCreator
  public Option(
    @JsonProperty("id") int id,
    @JsonProperty("value") String value,
    @JsonProperty("parentOptionId") int parentId
  ) {
    this.id = id;
    this.value = value;
    this.parentId = parentId;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }
}
