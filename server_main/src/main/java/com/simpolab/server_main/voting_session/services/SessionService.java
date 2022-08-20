package com.simpolab.server_main.voting_session.services;

import com.simpolab.server_main.voting_session.domain.VotingOption;
import com.simpolab.server_main.voting_session.domain.VotingSession;

public interface SessionService {
  void newSession(VotingSession newSession);
  void deleteSession(long sessionId);

  /*
    Handle Voting Groups
   */
  void addGroup(long sessionId, long groupId);
  void removeGroup(long sessionId, long groupId);

  /*
    Handle Voting Options
   */
  void newOption(long votingSessionId, String optionValue);

  void newOption(long votingSessionId, String optionValue, long parentOptionId);

  void removeOption(long optionId);

  /*
    Manage session lifecycle
   */
  void startSession(long sessionId);
  void endSession(long sessionId);
  void cancelSession(long sessionId);
  /*
    Handle the actual voting
   */
  // add vote
  //
}
