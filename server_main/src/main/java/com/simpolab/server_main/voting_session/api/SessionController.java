package com.simpolab.server_main.voting_session.api;

import com.simpolab.server_main.voting_session.domain.VotingSession;
import com.simpolab.server_main.voting_session.services.SessionService;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

  @DeleteMapping(
      path = "{sessionId}/group/{groupId}",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Void> removeElectorFromGroup(
      @PathVariable Long sessionId,
      @PathVariable Long groupId
  ) {
    sessionService.removeGroup(sessionId, groupId);
    return null;
  }

}
