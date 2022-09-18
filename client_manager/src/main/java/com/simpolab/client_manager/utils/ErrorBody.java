package com.simpolab.client_manager.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ErrorBody {

  @JsonProperty
  private int code;

  @JsonProperty
  private int winningTopOption;
}
