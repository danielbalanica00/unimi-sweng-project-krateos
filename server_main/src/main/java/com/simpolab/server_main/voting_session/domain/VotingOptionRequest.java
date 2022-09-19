package com.simpolab.server_main.voting_session.domain;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VotingOptionRequest {

  @NotBlank
  private String value;
}
