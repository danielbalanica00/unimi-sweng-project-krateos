package com.simpolab.client_elector.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class Session {

  public enum State {
    INACTIVE("INACTIVE"),
    ACTIVE("ACTIVE"),
    CANCELLED("CANCELLED"),
    ENDED("ENDED");

    private final String name;

    private State(String name) {
      this.name = name;
    }

    public String toString() {
      return this.name;
    }
  }

  public enum Type {
    ORDINAL("ORDINAL"),
    CATEGORIC_WITH_PREFERENCES("CATEGORIC_WITH_PREFERENCES"),
    CATEGORIC("CATEGORIC"),
    REFERENDUM("REFERENDUM");

    private final String name;

    private Type(String name) {
      this.name = name;
    }

    public String toString() {
      return this.name;
    }
  }

  private final String name;
  private final long endsOn;
  private final boolean needAbsoluteMajority;
  private final boolean hasQuorum;
  private final Type type;
  private State state;
  private long id;
  private List<Option> options;

  @JsonCreator
  public Session(
    @JsonProperty("name") String name,
    @JsonProperty("endsOn") long endsOn,
    @JsonProperty("needAbsoluteMajority") boolean needAbsoluteMajority,
    @JsonProperty("hasQuorum") boolean hasQuorum,
    @JsonProperty("type") String type
  ) {
    this.name = name;
    this.endsOn = endsOn;
    this.needAbsoluteMajority = needAbsoluteMajority;
    this.hasQuorum = hasQuorum;
    this.type = Type.valueOf(type);

    options = new ArrayList<>();
  }

  @JsonCreator
  public Session(
    @JsonProperty("name") String name,
    @JsonProperty("id") long id,
    @JsonProperty("endsOn") long endsOn,
    @JsonProperty("needAbsoluteMajority") boolean needAbsoluteMajority,
    @JsonProperty("hasQuorum") boolean hasQuorum,
    @JsonProperty("type") String type,
    @JsonProperty("state") String state
  ) {
    this.name = name;
    this.endsOn = endsOn;
    this.needAbsoluteMajority = needAbsoluteMajority;
    this.hasQuorum = hasQuorum;
    this.type = Type.valueOf(type);
    this.state = State.valueOf(state);
    this.id = id;

    options = new ArrayList<>();
  }

  public void setOptions(List<Option> options) {
    this.options = new ArrayList<>(options);
  }

  @Override
  public String toString() {
    return name + " [" + new Date(endsOn * 1000) + "]";
  }
}
