package com.simpolab.server_main.voting_session;

import com.simpolab.server_main.db.das.SessionDAS;
import com.simpolab.server_main.voting_session.domain.Vote;
import com.simpolab.server_main.voting_session.domain.VotingSession;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VoteValidator {

  private VoteValidator() {}

  @FunctionalInterface
  private interface Validator {
    boolean validate(List<Vote> votes, List<SessionDAS.Touple> options);
  }

  private static final Validator categoricValidator = (votes, options) -> {
    // only one answer is allowed
    if (votes.size() != 1) return false;

    // allowed options ids
    var optionsIds = options.stream().map(SessionDAS.Touple::id).collect(Collectors.toSet());

    // check if the voted option is present in the list of allowed options ids
    var votedOption = votes.get(0).getOptionId();
    return optionsIds.contains(votedOption);
  };

  private static final Validator categoricWithPreferencesValidator = (votes, options) -> {
    // at least one vote is needed
    if (votes.size() < 1) return false;

    // separate toplevel options from lower level options
    var topLevelOptions = new HashSet<SessionDAS.Touple>();
    var lowerLevelOptions = new HashSet<SessionDAS.Touple>();

    options.forEach(opt -> {
      if (opt.parentId() == 0) topLevelOptions.add(opt); else lowerLevelOptions.add(opt);
    });

    log.debug("All Options: {}", options);
    log.debug("Top Level Options: {}", topLevelOptions);
    log.debug("Lower Level Options: {}", lowerLevelOptions);

    // check that one vote is for a top level option
    var topLevelOptionsIds = topLevelOptions
      .stream()
      .map(SessionDAS.Touple::id)
      .collect(Collectors.toSet());
    var topLevelOptionsVotes = votes
      .stream()
      .filter(vote -> topLevelOptionsIds.contains(vote.getOptionId()))
      .toList();

    // cannot have more than one top level option
    if (topLevelOptionsVotes.size() != 1) return false;
    // if only vote was given and it's a top level option than the vote is valid
    if (votes.size() == 1) return true;

    // if more then one vote is present, one should be a top-level and the others should be under that top-level
    var topLevelOptionVoteId = topLevelOptionsVotes.get(0);
    var lowerLevelOptionsIds = lowerLevelOptions
      .stream()
      .filter(opt -> opt.parentId().equals(topLevelOptionVoteId.getOptionId()))
      .map(SessionDAS.Touple::id)
      .collect(Collectors.toUnmodifiableSet());
    var remainingVotes = votes
      .stream()
      .filter(vote -> !vote.equals(topLevelOptionVoteId))
      .map(Vote::getOptionId)
      .toList();

    return lowerLevelOptionsIds.containsAll(remainingVotes);
  };

  private static final Validator referendumValidator = (votes, options) -> {
    // only one answer is allowed
    if (votes.size() != 1) return false;

    // remove the question from the list of options so only yes and no remain
    var booleanOptions = options.stream().filter(vote -> vote.parentId() != null).toList();

    // check if the voted option is yer or no
    var votedOption = votes.get(0).getOptionId();
    return votedOption == booleanOptions.get(0).id() || votedOption == booleanOptions.get(1).id();
  };

  private static final Validator ordinalValidator = (votes, options) -> {
    // size of votes matches the num f options
    var optionsSize = options.size();
    var votesSize = votes.size();
    if (votesSize != optionsSize) return false;

    // the index numbers are from 1 to max
    var indexes = votes.stream().sorted().map(Vote::getOrderIndex).toList();
    var fstIdx = indexes.get(0);
    var lstIdx = indexes.get(votesSize - 1);
    if (fstIdx != 1 || lstIdx - fstIdx != optionsSize) return false;

    // the given votes have to be valid options
    var optionsIds = options.stream().map(SessionDAS.Touple::id).collect(Collectors.toSet());
    var votesOptionIds = votes.stream().map(Vote::getOptionId).collect(Collectors.toSet());
    return optionsIds.containsAll(votesOptionIds);
  };

  public static boolean validateVotes(
    VotingSession.Type sessionType,
    List<Vote> votes,
    List<SessionDAS.Touple> options
  ) {
    var validator =
      switch (sessionType) {
        case CATEGORIC -> categoricValidator;
        case CATEGORIC_WITH_PREFERENCES -> categoricWithPreferencesValidator;
        case ORDINAL -> ordinalValidator;
        case REFERENDUM -> referendumValidator;
      };

    var cleanVotes = votes.stream().distinct().toList();
    return validator.validate(cleanVotes, options);
  }
}
