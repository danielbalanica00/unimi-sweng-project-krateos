package com.simpolab.client_elector.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ErrorBody {

  @JsonProperty
  private int code;

  @JsonProperty
  private int winningTopOption;
}
