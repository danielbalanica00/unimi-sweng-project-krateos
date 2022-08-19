package com.simpolab.server_main.voting_session.domain;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VotingOption {

  @Min(0)
  private Long id;

  private VotingSession votingSession;

  @NotBlank
  private String optionValue;

  private VotingOption parent;
}
