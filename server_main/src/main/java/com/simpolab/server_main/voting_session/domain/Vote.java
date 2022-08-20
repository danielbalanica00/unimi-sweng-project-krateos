package com.simpolab.server_main.voting_session.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Vote {

  private Long optionId;
  private Long orderIndex;
}
