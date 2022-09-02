package com.simpolab.server_main.group.api;

import com.simpolab.server_main.elector.domain.NewElector;
import com.simpolab.server_main.group.domain.Group;
import com.simpolab.server_main.group.services.GroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/group")
@Slf4j
public class GroupController {

  @Autowired
  private GroupService groupService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<Group>> getGroups() {
    var groups = groupService.getGroups();
    return ResponseEntity.ok(groups);
  }

  @GetMapping(path = "{group_id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Group> getGroup(@PathVariable("group_id") Long id) {
    var group = groupService.getGroup(id);
    if (group == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    return ResponseEntity.ok(group);
  }

  @GetMapping(path = "{group_id}/elector", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<NewElector>> getElectorsInGroup(@PathVariable("group_id") Long id) {
    var electors = groupService.getElectorsInGroup(id);
    return ResponseEntity.ok(electors);
  }

  @PutMapping(path = "{groupId}/elector/{electorId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> insertElectorInGroup(
    @PathVariable Long groupId,
    @PathVariable Long electorId
  ) {
    groupService.addElector(groupId, electorId);

    return null;
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

    return null;
  }

  @DeleteMapping(path = "{group_id}")
  public ResponseEntity<Void> deleteGroup(@PathVariable("group_id") Long id) {
    groupService.deleteGroup(id);

    return null;
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> newElector(
    @Valid @RequestBody Group group,
    BindingResult bindingResult
  ) {
    if (bindingResult.hasErrors()) {
      log.warn("Error: {}", bindingResult.getAllErrors());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    try {
      groupService.newGroup(group.getName());
    } catch (Exception e) {
      log.error("Error ---> ", e);
      throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
    }
    return null;
  }
}
