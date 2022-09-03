package com.simpolab.server_main.voting_session.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VotingOption {
  private long id;

  private String value;

  private Long parentOptionId;

  public Long getParentOptionId() {
    return parentOptionId == 0 ? null : parentOptionId;
  }
}
