package com.simpolab.server_main.voting_session.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.Date;

@Data
@AllArgsConstructor
@Validated
@Builder
@Slf4j
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

  @JsonIgnore
  public Date getEndsOn() {
    return endsOn;
  }

  @JsonSetter("endsOn")
  public void setEndsOnFromEpoch(long epochTime) {
    this.endsOn = Date.from(Instant.ofEpochSecond(epochTime));
  }

  @JsonGetter("endsOn")
  public long getUnixTime() {
    return endsOn.getTime() / 1000;
  }

  @JsonIgnore
  public boolean isActive() {
    return state == State.ACTIVE;
  }
}
