package com.simpolab.client_elector.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Vote {

  private Integer optionId;
  private Integer orderIndex;

  @JsonCreator
  public Vote(
    @JsonProperty("optionId") Integer optionId,
    @JsonProperty("orderIndex") Integer orderIndex
  ) {
    this.optionId = optionId;
    this.orderIndex = orderIndex;
  }

  public Vote(Integer optionId) {
    this.optionId = optionId;
    this.orderIndex = 1;
  }

  @Override
  public String toString() {
    return "Vote{" + "optionId=" + optionId + ", orderIndex=" + orderIndex + '}';
  }
}
