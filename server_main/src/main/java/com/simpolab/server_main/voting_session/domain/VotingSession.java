package com.simpolab.server_main.voting_session.domain;

import java.util.Date;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@Validated
public class VotingSession {

  public enum Type {
    ORDINAL,
    CATEGORIC,
    CATEGORIC_WITH_PREFERENCES,
    REFERENDUM,
  }

  @Min(0)
  private Long id;

  @NotBlank
  private String name;

  private Date endsOn;
  private boolean isActive;
  private boolean isCancelled;
  private boolean hasEnded;
  private boolean needAbsoluteMajority;
  private boolean hasQuorum;
  private Type type;

  public VotingSession() {
    this.isActive = false;
    this.isCancelled = false;
    this.hasEnded = false;
    this.needAbsoluteMajority = false;
    this.hasQuorum = false;
  }
}
