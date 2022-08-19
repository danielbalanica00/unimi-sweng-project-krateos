package com.simpolab.server_main.voting_session.services;

import com.simpolab.server_main.voting_session.domain.VotingOption;
import com.simpolab.server_main.voting_session.domain.VotingSession;

public class SessionServiceImpl implements SessionService {

  @Override
  public VotingSession newSession(VotingSession newSession) {
    return null;
  }

  @Override
  public void deleteSession(long sessionId) {}

  @Override
  public void addGroup(long sessionId, long groupId) {}

  @Override
  public void removeGroup(long sessionId, long groupId) {}

  @Override
  public void newOption(VotingOption newOption) {}

  @Override
  public void newOption(VotingOption newOption, long parentOptionId) {}

  @Override
  public void removeOption(long optionId) {}

  @Override
  public void startSession(long sessionId) {}

  @Override
  public void endSession(long sessionId) {}

  @Override
  public void cancelSession(long sessionId) {}
}
