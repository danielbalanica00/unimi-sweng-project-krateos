package com.simpolab.server_main.voting_session.utils;

import com.simpolab.server_main.db.SessionDAO;
import com.simpolab.server_main.voting_session.domain.NoWinnerException;
import com.simpolab.server_main.voting_session.domain.ParticipationStats;
import com.simpolab.server_main.voting_session.domain.VotingSession;
import com.simpolab.server_main.voting_session.services.SessionService;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
public final class WinnerElection {

  private WinnerElection() {}

  @FunctionalInterface
  private interface Elector {
    List<Long> electWinner(
      SessionDAO sessionDAO,
      long sessionId,
      VotingSession session,
      ParticipationStats stats
    ) throws NoWinnerException;
  }

  private static final Elector categoricalAndReferendumElector = (
    sessionDAO,
    sessionId,
    session,
    stats
  ) -> {
    log.debug("REFERENDUM | CATEGORIC");

    Map<Long, Integer> votes = sessionDAO.getVotesPerOption(sessionId);
    if (votes.isEmpty()) throw new IllegalStateException("No votes found");

    log.debug("All VOTES: {}", votes);

    val mostVotedOptionId = getMostVotesOption(votes);
    val countMostVotedOption = countVotesForOption(votes, mostVotedOptionId);

    // check ballottaggio
    if (countMostVotedOption > 1) {
      log.debug("Ballottaggio, non c'e' vincitore");

      throw new NoWinnerException(NoWinnerException.BALLOTTAGGIO);
    }

    // check absolute majority
    if (
      session.isNeedAbsoluteMajority() &&
      !hasReachedAbsoluteMajority(stats.getVotersCount(), votes.get(mostVotedOptionId))
    ) {
      log.warn("Absolute majority not met for session {}, cancelling it", sessionId);

      throw new NoWinnerException(NoWinnerException.NO_ABSOLUTE_MAJORITY);
    }

    log.debug("Winner: {}", mostVotedOptionId);
    return List.of(mostVotedOptionId);
  };

  private static final Elector categoricalWithPreferencesElector = (
    sessionDAO,
    sessionId,
    session,
    stats
  ) -> {
    log.debug("CATEGORIC_WITH_PREFERENCES");

    // ** 1 dividi opzioni ottieni top level options
    val options2 = sessionDAO.getOptionsForSession(sessionId);
    var topLevelOptions = new HashSet<Long>();

    options2.forEach(opt -> {
      if (opt.parentId() == 0) topLevelOptions.add(opt.id());
    });
    // **** 1

    // ** 2 ottieni tutti i voti per la sessione
    Map<Long, Integer> votes = sessionDAO.getVotesPerOption(sessionId);
    if (votes.isEmpty()) throw new IllegalStateException("No votes found");

    Map<Long, Integer> topLevelVotes = new HashMap<>();
    Map<Long, Integer> lowLevelVotes = new HashMap<>();

    votes.forEach((k, v) -> {
      if (topLevelOptions.contains(k)) topLevelVotes.put(k, v); else lowLevelVotes.put(k, v);
    });
    // **** 2

    // ** 3 identifica top level con maggior numero di voti
    val topMostVotedOptionId = getMostVotesOption(topLevelVotes);
    val topCountMostVotedOption = countVotesForOption(topLevelVotes, topMostVotedOptionId);

    // check absolute majority
    if (
      session.isNeedAbsoluteMajority() &&
      !hasReachedAbsoluteMajority(stats.getVotersCount(), votes.get(topMostVotedOptionId))
    ) {
      log.warn("Absolute majority not met for session {}, cancelling it", sessionId);
      throw new NoWinnerException(NoWinnerException.NO_ABSOLUTE_MAJORITY);
    }

    // check ballottaggio
    if (topCountMostVotedOption > 1) {
      log.debug("Ballottaggio TOP level, non c'e' vincitore");

      throw new NoWinnerException(NoWinnerException.BALLOTTAGGIO);
    }
    // **** 3

    // ** 4 identifica low level con maggior numero di voti
    val lowMostVotedOptionId = getMostVotesOption(lowLevelVotes);
    val lowCountMostVotedOption = countVotesForOption(lowLevelVotes, lowMostVotedOptionId);

    // check absolute majority
    if (
      session.isNeedAbsoluteMajority() &&
      !hasReachedAbsoluteMajority(stats.getVotersCount(), votes.get(lowMostVotedOptionId))
    ) {
      log.warn("Absolute majority not met for session {}, cancelling it", sessionId);
      throw new NoWinnerException(NoWinnerException.NO_ABSOLUTE_MAJORITY);
    }

    // check ballottaggio
    if (lowCountMostVotedOption > 1) {
      log.debug("Ballottaggio LOW level, non c'e' vincitore");
      throw new NoWinnerException(
        NoWinnerException.BALLOTTAGGIO_CATEGORICO_PREFERENZE,
        topMostVotedOptionId
      );
    }
    // **** 4

    log.debug("Winners:  TOP - {}, LOW - {}", topMostVotedOptionId, lowMostVotedOptionId);
    return List.of(topMostVotedOptionId, lowMostVotedOptionId);
  };

  private static final Elector ordinalElector = (sessionDAO, sessionId, session, stats) -> {
    log.debug("ORDINAL");

    List<Long> excludedOptions = new ArrayList<>();
    while (true) {
      Map<Long, Integer> votes = sessionDAO.getVotesPerOptionOrdinal(sessionId, excludedOptions);
      if (votes.isEmpty()) throw new IllegalStateException("No votes found");

      log.debug("All VOTES: {}", votes);
      val mostVotedOptionId = getMostVotesOption(votes);
      val countMostVotedOption = countVotesForOption(votes, mostVotedOptionId);

      if (countMostVotedOption > 1) {
        log.debug("Ballottaggio ordinale");

        // get id of option with the lowest amount of votes
        val leastVotedOptionId = getLeastVotesOption(votes);

        // exclude it
        excludedOptions.add(leastVotedOptionId);

        // try again
        continue;
      }

      // check absolute majority
      if (
        session.isNeedAbsoluteMajority() &&
        !hasReachedAbsoluteMajority(stats.getVotersCount(), votes.get(mostVotedOptionId))
      ) {
        log.warn("Absolute majority not met for session {}, cancelling it", sessionId);
        throw new NoWinnerException(NoWinnerException.NO_ABSOLUTE_MAJORITY);
      }

      log.debug("Winner: {}", mostVotedOptionId);
      return List.of(mostVotedOptionId);
    }
  };

  private static long getLeastVotesOption(Map<Long, Integer> votes) {
    val optRes = votes
      .entrySet()
      .stream()
      .reduce((entry, acc) -> entry.getValue() <= acc.getValue() ? entry : acc);

    if (optRes.isEmpty()) throw new IllegalStateException("No minimum found");

    return optRes.get().getKey();
  }

  private static long getMostVotesOption(Map<Long, Integer> votes) {
    val optRes = votes
      .entrySet()
      .stream()
      .reduce((e, acc) -> e.getValue() >= acc.getValue() ? e : acc);

    if (optRes.isEmpty()) throw new IllegalStateException("No maximum found");

    return optRes.get().getKey();
  }

  private static int countVotesForOption(Map<Long, Integer> votes, long optionId) {
    val n = votes.get(optionId).intValue();

    return (int) votes.values().stream().filter(v -> v == n).count();
  }

  private static boolean hasReachedAbsoluteMajority(int votersCount, int obtainedVotes) {
    val threshold = votersCount / 2 + 1;

    return obtainedVotes >= threshold;
  }

  private static boolean hasReachedQuorum(ParticipationStats participationStats) {
    val totalVoters = participationStats.getVotersCount() + participationStats.getNonVotersCount();
    val threshold = totalVoters / 2 + 1;

    return participationStats.getVotersCount() >= threshold;
  }

  public static List<Long> getWinner(
    SessionService sessionService,
    SessionDAO sessionDAO,
    long sessionId
  ) throws NoWinnerException {
    val optSession = sessionDAO.get(sessionId);
    if (optSession.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

    val session = optSession.get();
    if (
      session.getState() == VotingSession.State.INACTIVE ||
      session.getState() == VotingSession.State.ACTIVE ||
      session.getState() == VotingSession.State.CANCELLED
    ) throw new ResponseStatusException(HttpStatus.FORBIDDEN);

    val stats = sessionDAO.getParticipationStats(sessionId);

    // ** Quorum Check
    if (session.isHasQuorum() && !hasReachedQuorum(stats)) {
      log.warn("Quorum not met for session {}, cancelling it", sessionId);
      sessionService.setState(sessionId, VotingSession.State.INVALID);
      throw new NoWinnerException(NoWinnerException.QUORUM_NOT_REACHED);
    }
    // ****

    val elector =
      switch (session.getType()) {
        case REFERENDUM, CATEGORIC -> categoricalAndReferendumElector;
        case ORDINAL -> ordinalElector;
        case CATEGORIC_WITH_PREFERENCES -> categoricalWithPreferencesElector;
      };

    try {
      return elector.electWinner(sessionDAO, sessionId, session, stats);
    } catch (NoWinnerException nwe) {
      if (nwe.getCode() == NoWinnerException.BALLOTTAGGIO_CATEGORICO_PREFERENZE) throw nwe;

      sessionService.setState(sessionId, VotingSession.State.INVALID);
      throw nwe;
    }
  }
}
