package com.simpolab.client_manager.session;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Session {

  public enum SessionType {
    ORDINAL("ORDINAL"),
    CATEGORIC_WITH_PREFERENCES("CATEGORIC_WITH_PREFERENCES"),
    CATEGORIC("CATEGORIC"),
    REFERENDUM("REFERENDUM");

    private final String name;

    private SessionType(String name) {
      this.name = name;
    }

    public String toString() {
      return this.name;
    }
  }

  private String name;
  private String endsOn;
  private boolean needAbsoluteMajority;
  private boolean hasQuorum;
  private String type;

  @JsonCreator
  public Session(
    @JsonProperty("name") String name,
    @JsonProperty("endsOn") String endsOn,
    @JsonProperty("needAbsoluteMajority") boolean needAbsoluteMajority,
    @JsonProperty("hasQuorum") boolean hasQuorum,
    @JsonProperty("type") String type
  ) {
    this.name = name;
    this.endsOn = endsOn;
    this.needAbsoluteMajority = needAbsoluteMajority;
    this.hasQuorum = hasQuorum;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEndsOn() {
    return endsOn;
  }

  public void setEndsOn(String endsOn) {
    this.endsOn = endsOn;
  }

  public boolean isNeedAbsoluteMajority() {
    return needAbsoluteMajority;
  }

  public void setNeedAbsoluteMajority(boolean needAbsoluteMajority) {
    this.needAbsoluteMajority = needAbsoluteMajority;
  }

  public boolean isHasQuorum() {
    return hasQuorum;
  }

  public void setHasQuorum(boolean hasQuorum) {
    this.hasQuorum = hasQuorum;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
