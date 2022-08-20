package com.simpolab.server_main.voting_session.domain;

import java.util.Objects;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Vote implements Comparable {

  private Long optionId;
  private Long orderIndex;

  @Override
  public int compareTo(Object o) {
    if (this == o) return 0;
    if (o == null || getClass() != o.getClass()) return -1;

    Vote other = (Vote) o;
    return Long.compare(this.getOrderIndex(), other.getOrderIndex());
  }

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
}
