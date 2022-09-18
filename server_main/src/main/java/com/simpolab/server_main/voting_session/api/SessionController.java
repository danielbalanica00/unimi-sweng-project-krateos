package com.simpolab.server_main.voting_session.api;

import com.simpolab.server_main.group.domain.Group;
import com.simpolab.server_main.voting_session.domain.*;
import com.simpolab.server_main.voting_session.services.SessionService;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/session")
@Slf4j
public class SessionController {

  @Autowired
  private SessionService sessionService;

  @PostMapping(
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<VotingSession> newSession(
    @Valid @RequestBody VotingSession votingSession,
    BindingResult bindingResult
  ) {
    if (bindingResult.hasErrors()) {
      log.debug("[New Session] - Validation error: {}", bindingResult.getAllErrors());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    try {
      log.debug("[New Session] - {}", votingSession);
      var newSession = sessionService.newSession(votingSession);
      return ResponseEntity.ok(newSession);
    } catch (Exception e) {
      log.error("[New Session] - Error:  ", e);
      throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
    }
  }

  @GetMapping(path = "{session_id}")
  public ResponseEntity<VotingSession> getSession(@PathVariable("session_id") long sessionId) {
    try {
      //      sessionService.getWinner(sessionId);
      log.debug("[Get Session] - get session {}", sessionId);
      return ResponseEntity.ok(sessionService.getSession(sessionId));
    } catch (Exception e) {
      log.error("[Get Session] - session {} not found", sessionId, e);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping
  public ResponseEntity<List<VotingSession>> getAllSessions() {
    log.debug("[Get All Sessions] - getting all the sessions");
    return ResponseEntity.ok(sessionService.getAllSessions());
  }

  @GetMapping(path = "/elector/{electorId}")
  public ResponseEntity<List<VotingSession>> getAllSessionsForElector(
    @PathVariable("electorId") long electorId
  ) {
    log.debug("[Get All Sessions] - getting all the sessions for elector {}", electorId);
    return ResponseEntity.ok(sessionService.getAllSessions(electorId));
  }

  @GetMapping(path = "{sessionId}/result/option")
  public ResponseEntity<Map<Long, Integer>> getOptionCount(
    @PathVariable("sessionId") long sessionId
  ) {
    log.debug("[Get Option Count] - getting the option count");
    return ResponseEntity.ok(sessionService.votesPerOption(sessionId));
  }

  @GetMapping(path = "{sessionId}/result/winner")
  public ResponseEntity getWinningOption(@PathVariable("sessionId") long sessionId) {
    log.debug("[Get Winning Option] - start");

    try {
      val winningOptionId = sessionService.getWinner(sessionId);

      log.debug("[Get Winning Option] - result : {}", winningOptionId);
      return ResponseEntity.ok(winningOptionId);
    } catch (NoWinnerException nwe) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(nwe);
    }
  }

  @DeleteMapping(path = "{session_id}")
  public ResponseEntity<Void> deleteElector(@PathVariable("session_id") Long sessionId) {
    try {
      log.debug("[Delete Session] - delete session {}", sessionId);
      sessionService.deleteSession(sessionId);
    } catch (Exception e) {
      log.error("[Delete Session] - session {} not found", sessionId, e);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return ResponseEntity.ok().build();
  }

  @PutMapping(path = "{sessionId}/group/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> addGroupToSession(
    @PathVariable Long sessionId,
    @PathVariable Long groupId
  ) {
    // todo could fail if group doesnt exist
    log.debug("[Add Group to Session] - adding group {} to session {}", groupId, sessionId);
    sessionService.addGroup(sessionId, groupId);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping(path = "{sessionId}/group/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> removeGroupFromSession(
    @PathVariable Long sessionId,
    @PathVariable Long groupId
  ) {
    // todo could fail if group or session dont exist
    log.debug(
      "[Remove Group from Session] - removing group {} from session {}",
      groupId,
      sessionId
    );
    sessionService.removeGroup(sessionId, groupId);
    return ResponseEntity.ok().build();
  }

  @PutMapping(path = "{sessionId}/option", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> addOption(
    @PathVariable Long sessionId,
    @RequestBody VotingOptionRequest vor
  ) {
    log.debug("[Add Option] - session {} new option {}", sessionId, vor.getValue());

    sessionService.newOption(sessionId, vor.getValue());
    return null;
  }

  @DeleteMapping(path = "option/{optionId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> removeOption(@PathVariable Long optionId) {
    log.debug("[remove option] - remove option {}", optionId);

    sessionService.removeOption(optionId);

    return ResponseEntity.ok().build();
  }

  @GetMapping(path = "{sessionId}/option", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<VotingOption>> getOptions(@PathVariable Long sessionId) {
    log.debug("[Get Options] - session {}", sessionId);

    var votingOptions = sessionService.getOptions(sessionId);

    return ResponseEntity.ok(votingOptions);
  }

  @GetMapping(path = "{sessionId}/group", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<Group>> getGroups(@PathVariable Long sessionId) {
    log.debug("[Get Groups] - session {}", sessionId);

    var groups = sessionService.getGroups(sessionId);

    return ResponseEntity.ok(groups);
  }

  @PutMapping(
    path = "{sessionId}/option/{optionId}/suboption",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Void> insertSubOptionInVotingSession(
    @PathVariable Long sessionId,
    @PathVariable Long optionId,
    @RequestBody VotingOptionRequest vor
  ) {
    log.debug(
      "[Add Suboption] - session {}, option {}, new suboption {}",
      sessionId,
      optionId,
      vor.getValue()
    );

    sessionService.newOption(sessionId, vor.getValue(), optionId);
    return null;
  }

  @PatchMapping(
    path = "{sessionId}/state/{newStateString}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Void> setState(
    @PathVariable Long sessionId,
    @PathVariable String newStateString
  ) {
    try {
      log.debug("[Set State] - received {}", newStateString);
      var newState = VotingSession.State.valueOf(newStateString.trim().toUpperCase());
      log.debug("[Set State] - setting session {} state to {}", sessionId, newStateString);
      sessionService.setState(sessionId, newState);
      log.debug("[Set State] - state set successfully");
    } catch (IllegalArgumentException iae) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      log.error("Error ---> ", e);
      throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
    }

    return ResponseEntity.ok().build();
  }

  @PostMapping(
    path = "{sessionId}/vote",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Void> newVote(
    Authentication authentication,
    @PathVariable Long sessionId,
    @RequestBody List<Vote> votes
  ) {
    try {
      var username = (String) authentication.getPrincipal();

      sessionService.expressVote(username, sessionId, votes);
    } catch (Exception e) {
      log.error("Error ---> ", e);
      throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
    }
    return null;
  }
}
