package com.simpolab.server_main.elector.api;

import com.simpolab.server_main.auth.authorizers.IsManager;
import com.simpolab.server_main.elector.domain.Elector;
import com.simpolab.server_main.elector.services.ElectorService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RequiredArgsConstructor
@RestController
@IsManager
@RequestMapping("/api/v1/elector")
public class ElectorController {

  private final ElectorService electorService;

  @GetMapping(path = "{elector_id}")
  public ResponseEntity<Elector> getElector(@PathVariable("elector_id") long electorId) {
    var elector = electorService.getElector(electorId);

    if (elector == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    return ResponseEntity.ok(elector);
  }

  @GetMapping
  public ResponseEntity<List<Elector>> getElectors() {
    var electors = electorService.getElectors();
    return ResponseEntity.ok(electors);
  }

  @DeleteMapping(path = "{elector_id}")
  public ResponseEntity<Void> deleteElector(@PathVariable("elector_id") long electorId) {
    try {
      electorService.deleteElector(electorId);
    } catch (Exception e) {
      log.error("Elector {} not found", electorId, e);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return ResponseEntity.ok().build();
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> newElector(
    @Valid @RequestBody Elector elector,
    BindingResult bindingResult
  ) {
    if (bindingResult.hasErrors()) {
      log.debug("[New Elector] - Validation error: {}", bindingResult.getAllErrors());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    try {
      log.debug("[New Elector] -  {}", elector);
      electorService.newElector(elector);
    } catch (Exception e) {
      log.error("[New Elector] - Error:  ", e);
      throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
    }

    return ResponseEntity.ok().build();
  }
}
