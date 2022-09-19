package com.simpolab.server_main.group.api;

import com.simpolab.server_main.auth.authorizers.IsManager;
import com.simpolab.server_main.elector.domain.NewElector;
import com.simpolab.server_main.group.domain.Group;
import com.simpolab.server_main.group.services.GroupService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/group")
@IsManager
public class GroupController {

  private final GroupService groupService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<Group>> getGroups() {
    var groups = groupService.getGroups();
    return ResponseEntity.ok(groups);
  }

  @GetMapping(path = "{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Group> getGroup(@PathVariable("groupId") Long groupId) {
    var group = groupService.getGroup(groupId);

    if (group == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    return ResponseEntity.ok(group);
  }

  @GetMapping(path = "{groupId}/elector", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<NewElector>> getElectorsInGroup(
    @PathVariable("groupId") Long groupId
  ) {
    var electors = groupService.getElectorsInGroup(groupId);
    return ResponseEntity.ok(electors);
  }

  @PutMapping(path = "{groupId}/elector/{electorId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> insertElectorInGroup(
    @PathVariable Long groupId,
    @PathVariable Long electorId
  ) {
    groupService.addElector(groupId, electorId);

    return ResponseEntity.ok().build();
  }

  @DeleteMapping(
    path = "{groupId}/elector/{electorId}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Void> removeElectorFromGroup(
    @PathVariable Long groupId,
    @PathVariable Long electorId
  ) {
    groupService.removeElector(groupId, electorId);

    return ResponseEntity.ok().build();
  }

  @DeleteMapping(path = "{groupId}")
  public ResponseEntity<Void> deleteGroup(@PathVariable("groupId") Long groupId) {
    groupService.deleteGroup(groupId);

    return ResponseEntity.ok().build();
  }

  @PostMapping(
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Group> newGroup(
    @Valid @RequestBody Group group,
    BindingResult bindingResult
  ) {
    if (bindingResult.hasErrors()) {
      log.warn("Error: {}", bindingResult.getAllErrors());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    try {
      var newGroup = groupService.newGroup(group.getName());
      return ResponseEntity.ok(newGroup);
    } catch (DuplicateKeyException e) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    } catch (Exception e) {
      log.error("Error ---> ", e);
      throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
    }
  }
}
