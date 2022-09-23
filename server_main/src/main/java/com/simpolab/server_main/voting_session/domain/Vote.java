package com.simpolab.server_main.voting_session.domain;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vote implements Comparable<Vote> {

  private Long optionId;
  private Long orderIndex;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Vote vote = (Vote) o;
    return optionId.equals(vote.optionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(optionId);
  }

  @Override
  public int compareTo(Vote other) {
    return Long.compare(this.orderIndex, other.orderIndex);
  }
}
