package com.simpolab.server_main.voting_session.api;

import com.simpolab.server_main.voting_session.domain.Vote;
import com.simpolab.server_main.voting_session.domain.VotingOptionRequest;
import com.simpolab.server_main.voting_session.domain.VotingSession;
import com.simpolab.server_main.voting_session.services.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

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
  public ResponseEntity<Void> newSession(
    @Valid @RequestBody VotingSession votingSession,
    BindingResult bindingResult
  ) {
    if (bindingResult.hasErrors()) {
      log.warn("Error: {}", bindingResult.getAllErrors());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    try {
      sessionService.newSession(votingSession);
    } catch (Exception e) {
      log.error("Error ---> ", e);
      throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
    }
    return null;
  }

  @GetMapping(path = "{session_id}")
  public ResponseEntity<VotingSession> getSession(@PathVariable("session_id") long sessionId) {
    try {
      return ResponseEntity.ok(sessionService.getSession(sessionId));
    } catch (Exception e) {
      log.error("Session {} not found", sessionId, e);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping
  public ResponseEntity<List<VotingSession>> getSession() {
    return ResponseEntity.ok(sessionService.getAllSessions());
  }

  @DeleteMapping(path = "{session_id}")
  public ResponseEntity<Void> deleteElector(@PathVariable("session_id") Long id) {
    try {
      sessionService.deleteSession(id);
    } catch (Exception e) {
      log.error("Session {} not found", id, e);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return null;
  }

  @PutMapping(path = "{sessionId}/group/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> insertElectorInGroup(
    @PathVariable Long sessionId,
    @PathVariable Long groupId
  ) {
    sessionService.addGroup(sessionId, groupId);
    return null;
  }

  @DeleteMapping(path = "{sessionId}/group/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> removeElectorFromGroup(
    @PathVariable Long sessionId,
    @PathVariable Long groupId
  ) {
    sessionService.removeGroup(sessionId, groupId);
    return null;
  }

  @PutMapping(path = "{sessionId}/option", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> insertOptionInVotingSession(
    @PathVariable Long sessionId,
    @RequestBody VotingOptionRequest vor
  ) {
    log.info("Session ID: {}, Value: {}", sessionId, vor.getValue());

    sessionService.newOption(sessionId, vor.getValue());
    return null;
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
    log.info("Session ID: {}, Parent Option: {}, Value: {}", sessionId, optionId, vor.getValue());

    sessionService.newOption(sessionId, vor.getValue(), optionId);
    return null;
  }

  @PatchMapping(path = "{sessionId}/start", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> startSession(@PathVariable Long sessionId) {
    log.info("Starting session with id: {}", sessionId);

    sessionService.startSession(sessionId);
    return null;
  }

  @PatchMapping(path = "{sessionId}/end", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> endSession(@PathVariable Long sessionId) {
    log.info("Ending session with id: {}", sessionId);

    sessionService.endSession(sessionId);
    return null;
  }

  @PatchMapping(path = "{sessionId}/cancel", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> cancelSession(@PathVariable Long sessionId) {
    log.info("Canceling session with id: {}", sessionId);

    sessionService.cancelSession(sessionId);
    return null;
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
