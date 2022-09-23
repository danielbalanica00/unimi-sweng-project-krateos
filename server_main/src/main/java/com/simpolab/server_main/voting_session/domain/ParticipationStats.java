package com.simpolab.server_main.voting_session.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationStats {

  private int votersCount;
  private int nonVotersCount;
}
