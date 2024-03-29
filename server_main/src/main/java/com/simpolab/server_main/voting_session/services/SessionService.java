package com.simpolab.server_main.voting_session.services;

import com.simpolab.server_main.group.domain.Group;
import com.simpolab.server_main.voting_session.domain.NoWinnerException;
import com.simpolab.server_main.voting_session.domain.Vote;
import com.simpolab.server_main.voting_session.domain.VotingOption;
import com.simpolab.server_main.voting_session.domain.VotingSession;
import java.util.List;
import java.util.Map;

public interface SessionService {
  /*
    Handle Sessions
   */
  VotingSession newSession(VotingSession newSession);

  void deleteSession(long sessionId);

  VotingSession getSession(long sessionId);

  List<VotingSession> getAllSessions();

  List<VotingSession> getAllSessions(String electorUsername);

  void setState(long sessionId, VotingSession.State newState);

  Map<Long, Integer> votesPerOption(long sessionId);

  List<Long> getWinner(long sessionId) throws NoWinnerException;

  /*
    Handle Voting Groups
   */
  void addGroup(long sessionId, long groupId);

  void removeGroup(long sessionId, long groupId);

  List<Group> getGroups(long sessionId);

  /*
    Handle Voting Options
   */
  void newOption(long votingSessionId, String optionValue);

  void newOption(long votingSessionId, String optionValue, long parentOptionId);

  void removeOption(long optionId);

  List<VotingOption> getOptions(long sessionId);

  /*
    Handle the actual voting
   */
  void expressVote(String electorUsername, long sessionId, List<Vote> votes);
}
