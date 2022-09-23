package com.simpolab.server_main;

import com.simpolab.server_main.db.SessionDAO;
import com.simpolab.server_main.voting_session.domain.*;
import java.sql.SQLException;
import java.util.*;
import lombok.val;

public class SessionDAOMock implements SessionDAO {

  private final Set<VotingSession> sessions = new HashSet<>();

  private final Map<Long, List<VotingOption>> options = new HashMap<>();

  private final Map<Long, List<Vote>> votes = new HashMap<>();

  private ParticipationStats stats;

  private static long optionIdCounter = 1;

  @Override
  public long create(VotingSession newSession) throws SQLException {
    //    newSession.setId(1L);

    val res = sessions.add(newSession);
    if (!res) {
      sessions.remove(newSession);
      sessions.add(newSession);
    } else {
      optionIdCounter = 1;
      options.put(newSession.getId(), new ArrayList<>());
      votes.put(newSession.getId(), new ArrayList<>());
    }

    System.out.println(sessions);
    return newSession.getId();
  }

  @Override
  public void delete(long id) {}

  @Override
  public Optional<VotingSession> get(long id) {
    return sessions.stream().filter(session -> session.getId() == id).findFirst();
  }

  @Override
  public List<VotingSession> getAll() {
    return sessions.stream().toList();
  }

  @Override
  public List<VotingSession> getAll(long electorId) {
    return null;
  }

  @Override
  public void addGroup(long sessionId, long groupId) throws SQLException {}

  @Override
  public void removeGroup(long sessionId, long groupId) {}

  @Override
  public void createOption(long votingSessionId, String optionValue) throws SQLException {
    val option = new VotingOption(SessionDAOMock.optionIdCounter++, optionValue, 0L);
    System.out.println("options parent: " + option.getParentOptionId());

    options.get(votingSessionId).add(option);
    System.out.println("Options: " + options);
  }

  @Override
  public void createOption(long votingSessionId, String optionValue, long parentOptionId)
    throws SQLException {
    val option = new VotingOption(SessionDAOMock.optionIdCounter++, optionValue, parentOptionId);
    System.out.println("options parent: " + option.getParentOptionId());
    options.get(votingSessionId).add(option);
    System.out.println("Options: " + options);
  }

  @Override
  public void deleteOption(long optionId) {}

  @Override
  public List<VotingOption> getOptions(long votingSessionId) {
    return null;
  }

  @Override
  public List<VotingOptionId> getOptionsForSession(long sessionId) {
    System.out.println(options);

    return options
      .entrySet()
      .stream()
      .filter(entry -> entry.getKey().equals(sessionId))
      .map(Map.Entry::getValue)
      .findFirst()
      .get()
      .stream()
      .map(votingOption -> {
        long parent = votingOption.getParentOptionId() == null
          ? 0
          : votingOption.getParentOptionId();

        return new VotingOptionId(votingOption.getId(), parent);
      })
      .toList();
  }

  @Override
  public void setState(long sessionId, VotingSession.State newState) throws SQLException {
    get(sessionId).get().setState(newState);
  }

  @Override
  public void populateSessionParticipants(long sessionId) throws SQLException {}

  @Override
  public Optional<Boolean> getParticipationStatus(long sessionId, long electorId) {
    return Optional.empty();
  }

  @Override
  public void addVotes(long sessionId, List<Vote> votes) throws SQLException {
    System.out.println("VOTES: " + this.votes.get(sessionId));
    this.votes.get(sessionId).addAll(votes);
    System.out.println("VOTESAFTER: " + this.votes.get(sessionId));
  }

  @Override
  public void setHasVoted(long sessionId, long electorId) throws SQLException {}

  @Override
  public Map<Long, Integer> getVotesPerOption(long sessionId) {
    val sessionOptions = options.get(sessionId);
    val resMap = new HashMap<Long, Integer>();

    System.out.println("options" + sessionOptions);

    sessionOptions.forEach(opt -> resMap.put(opt.getId(), 0));

    System.out.println("votes " + votes);
    votes
      .get(sessionId)
      .forEach(vote -> resMap.put(vote.getOptionId(), resMap.get(vote.getOptionId()) + 1));

    System.out.println(resMap);
    return resMap;
  }

  @Override
  public Map<Long, Integer> getVotesPerOptionOrdinal(long sessionId, List<Long> excludedIds) {
    val sessionOptions = options.get(sessionId);
    val resMap = new HashMap<Long, Integer>();

    System.out.println("options" + sessionOptions);

    sessionOptions.forEach(opt -> resMap.put(opt.getId(), 0));

    System.out.println("votes " + votes);
    votes
      .get(sessionId)
      .stream()
      .filter(vote -> vote.getOrderIndex() == 1)
      //        .filter(vote -> !excludedIds.contains(vote.getOptionId()))
      .forEach(vote -> resMap.put(vote.getOptionId(), resMap.get(vote.getOptionId()) + 1));

    System.out.println(resMap);
    return resMap;
  }

  public void setVotes(long sessionId, List<Vote> votes) throws SQLException {
    this.votes.get(sessionId).clear();
    //    System.out.println("VOTES: " + this.votes.get(sessionId));
    addVotes(sessionId, votes);
    //    System.out.println("VOTESAFTER: " + this.votes.get(sessionId));
  }

  @Override
  public ParticipationStats getParticipationStats(long sessionId) {
    return stats;
  }

  public void setParticipationsStats(ParticipationStats stats) {
    this.stats = stats;
  }
}
