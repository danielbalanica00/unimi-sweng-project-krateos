package com.simpolab.server_main.voting_session.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import java.time.Instant;
import java.util.Date;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
    INVALID,
  }

  @Min(0)
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long id;

  @NotBlank
  private String name;

  @Future
  private Date endsOn;

  private boolean needAbsoluteMajority = false;
  private boolean hasQuorum = false;

  private Type type;

  private State state = State.INACTIVE;

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
