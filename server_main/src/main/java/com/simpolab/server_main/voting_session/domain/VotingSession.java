package com.simpolab.server_main.voting_session.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@Validated
@Builder
public class VotingSession {

  public enum Type {
    ORDINAL,
    CATEGORIC,
    CATEGORIC_WITH_PREFERENCES,
    REFERENDUM,
  }

  public enum State {
    INACTIVE,
    ACTIVE,
    CANCELLED,
    ENDED,
  }

  @Min(0)
  private Long id;

  @NotBlank
  private String name;

  private Date endsOn;

  private boolean needAbsoluteMajority;
  private boolean hasQuorum;
  private Type type;

  private State state;

  public VotingSession() {
    this.state = State.INACTIVE;
    this.needAbsoluteMajority = false;
    this.hasQuorum = false;
  }

  @JsonGetter("endsOn")
  public long getEndsOn() {
    return endsOn.getTime();
  }

  @JsonIgnore
  public boolean isActive() {
    return state == State.ACTIVE;
  }
}
