package com.simpolab.server_main.voting_session.domain;

import lombok.Data;

@Data
public class ParticipationStats {

  private int votersCount;
  private int nonVotersCount;
}
