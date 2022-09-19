package com.simpolab.server_main.voting_session.api;

import com.simpolab.server_main.auth.authorizers.IsElector;
import com.simpolab.server_main.auth.authorizers.IsManager;
import com.simpolab.server_main.group.domain.Group;
import com.simpolab.server_main.voting_session.domain.*;
import com.simpolab.server_main.voting_session.services.SessionService;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/session")
public class SessionController {

  private final SessionService sessionService;

  @IsManager
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

  @IsManager
  @GetMapping(path = "{sessionId}")
  public ResponseEntity<VotingSession> getSession(@PathVariable("sessionId") long sessionId) {
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
  public ResponseEntity<List<VotingSession>> getAllSessions(Authentication authentication) {
    boolean isManager = authentication
      .getAuthorities()
      .stream()
      .anyMatch(r -> r.getAuthority().equals("MANAGER"));

    if (isManager) {
      log.debug("[Get All Sessions - MANAGER] - getting all the sessions");
      return ResponseEntity.ok(sessionService.getAllSessions());
    }

    log.debug("[Get All Sessions] - getting all the sessions for elector");
    val electorUsername = (String) authentication.getPrincipal();

    return ResponseEntity.ok(sessionService.getAllSessions(electorUsername));
  }

  @IsManager
  @GetMapping(path = "{sessionId}/result/option")
  public ResponseEntity<Map<Long, Integer>> getOptionCount(
    @PathVariable("sessionId") long sessionId
  ) {
    log.debug("[Get Option Count] - getting the option count");
    return ResponseEntity.ok(sessionService.votesPerOption(sessionId));
  }

  @IsManager
  @GetMapping(path = "{sessionId}/result/winner")
  public ResponseEntity<?> getWinningOption(@PathVariable("sessionId") long sessionId) {
    log.debug("[Get Winning Option] - start");

    try {
      val winningOptionId = sessionService.getWinner(sessionId);

      log.debug("[Get Winning Option] - result : {}", winningOptionId);
      return ResponseEntity.ok(winningOptionId);
    } catch (NoWinnerException nwe) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(nwe);
    }
  }

  @IsManager
  @DeleteMapping(path = "{sessionId}")
  public ResponseEntity<Void> deleteElector(@PathVariable("sessionId") long sessionId) {
    try {
      log.debug("[Delete Session] - delete session {}", sessionId);
      sessionService.deleteSession(sessionId);
    } catch (Exception e) {
      log.error("[Delete Session] - session {} not found", sessionId, e);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return ResponseEntity.ok().build();
  }

  @IsManager
  @PutMapping(path = "{sessionId}/group/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> addGroupToSession(
    @PathVariable long sessionId,
    @PathVariable long groupId
  ) {
    // todo could fail if group doesnt exist
    log.debug("[Add Group to Session] - adding group {} to session {}", groupId, sessionId);
    sessionService.addGroup(sessionId, groupId);
    return ResponseEntity.ok().build();
  }

  @IsManager
  @DeleteMapping(path = "{sessionId}/group/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> removeGroupFromSession(
    @PathVariable long sessionId,
    @PathVariable long groupId
  ) {
    log.debug(
      "[Remove Group from Session] - removing group {} from session {}",
      groupId,
      sessionId
    );
    sessionService.removeGroup(sessionId, groupId);
    return ResponseEntity.ok().build();
  }

  @IsManager
  @PutMapping(path = "{sessionId}/option", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> addOption(
    @PathVariable long sessionId,
    @RequestBody @Valid VotingOptionRequest vor
  ) {
    log.debug("[Add Option] - session {} new option {}", sessionId, vor.getValue());

    sessionService.newOption(sessionId, vor.getValue());
    return null;
  }

  @IsManager
  @DeleteMapping(path = "option/{optionId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> removeOption(@PathVariable long optionId) {
    log.debug("[remove option] - remove option {}", optionId);

    sessionService.removeOption(optionId);

    return ResponseEntity.ok().build();
  }

  @GetMapping(path = "{sessionId}/option", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<VotingOption>> getOptions(@PathVariable long sessionId) {
    log.debug("[Get Options] - session {}", sessionId);

    var votingOptions = sessionService.getOptions(sessionId);

    return ResponseEntity.ok(votingOptions);
  }

  @IsManager
  @GetMapping(path = "{sessionId}/group", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<Group>> getGroups(@PathVariable long sessionId) {
    log.debug("[Get Groups] - session {}", sessionId);

    var groups = sessionService.getGroups(sessionId);

    return ResponseEntity.ok(groups);
  }

  @IsManager
  @PutMapping(
    path = "{sessionId}/option/{optionId}/suboption",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Void> insertSubOptionInVotingSession(
    @PathVariable long sessionId,
    @PathVariable long optionId,
    @RequestBody @Valid VotingOptionRequest vor
  ) {
    log.debug(
      "[Add Suboption] - session {}, option {}, new suboption {}",
      sessionId,
      optionId,
      vor.getValue()
    );

    sessionService.newOption(sessionId, vor.getValue(), optionId);
    return ResponseEntity.ok().build();
  }

  @IsManager
  @PatchMapping(
    path = "{sessionId}/state/{newStateString}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Void> setState(
    @PathVariable long sessionId,
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

  @IsElector
  @PostMapping(
    path = "{sessionId}/vote",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Void> newVote(
    Authentication authentication,
    @PathVariable long sessionId,
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
