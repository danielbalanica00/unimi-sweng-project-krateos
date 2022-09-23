package com.simpolab.server_main;

import com.simpolab.server_main.group.domain.Group;
import com.simpolab.server_main.voting_session.domain.NoWinnerException;
import com.simpolab.server_main.voting_session.domain.Vote;
import com.simpolab.server_main.voting_session.domain.VotingOption;
import com.simpolab.server_main.voting_session.domain.VotingSession;
import com.simpolab.server_main.voting_session.services.SessionService;
import java.util.List;
import java.util.Map;

public class SessionServiceMock implements SessionService {

  @Override
  public VotingSession newSession(VotingSession newSession) {
    return null;
  }

  @Override
  public void deleteSession(long sessionId) {}

  @Override
  public VotingSession getSession(long sessionId) {
    return null;
  }

  @Override
  public List<VotingSession> getAllSessions() {
    return null;
  }

  @Override
  public List<VotingSession> getAllSessions(String electorUsername) {
    return null;
  }

  @Override
  public void setState(long sessionId, VotingSession.State newState) {}

  @Override
  public Map<Long, Integer> votesPerOption(long sessionId) {
    return null;
  }

  @Override
  public List<Long> getWinner(long sessionId) throws NoWinnerException {
    return null;
  }

  @Override
  public void addGroup(long sessionId, long groupId) {}

  @Override
  public void removeGroup(long sessionId, long groupId) {}

  @Override
  public List<Group> getGroups(long sessionId) {
    return null;
  }

  @Override
  public void newOption(long votingSessionId, String optionValue) {}

  @Override
  public void newOption(long votingSessionId, String optionValue, long parentOptionId) {}

  @Override
  public void removeOption(long optionId) {}

  @Override
  public List<VotingOption> getOptions(long sessionId) {
    return null;
  }

  @Override
  public void expressVote(String electorUsername, long sessionId, List<Vote> votes) {}
}
