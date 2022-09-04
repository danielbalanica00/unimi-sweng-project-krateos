package com.simpolab.client_manager.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class Option {

  @Getter
  private Integer id;
  @Getter @Setter
  private String value;
  private Integer parentOptionId;

  public Option(String value) {
    this.value = value;
  }

  @JsonCreator
  public Option(@JsonProperty("id") int id, @JsonProperty("value") String value) {
    this.id = id;
    this.value = value;
    this.parentOptionId = -1;
  }

  @JsonCreator
  public Option(
    @JsonProperty("id") int id,
    @JsonProperty("value") String value,
    @JsonProperty("parentOptionId") int parentOptionId
  ) {
    this.id = id;
    this.value = value;
    this.parentOptionId = parentOptionId;
  }

  public Integer getParentOptionId() {
    return parentOptionId == null ? -1 : parentOptionId;
  }

  @Override
  public String toString() {
    return value;
  }

//  @Override
//  public String toString() {
//    return "Option{" +
//            "id=" + id +
//            ", value='" + value + '\'' +
//            ", parentId=" + parentOptionId +
//            '}';
//  }
}
