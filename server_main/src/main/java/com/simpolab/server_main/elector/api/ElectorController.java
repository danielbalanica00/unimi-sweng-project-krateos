package com.simpolab.server_main.elector.api;

import com.simpolab.server_main.elector.domain.NewElector;
import com.simpolab.server_main.elector.services.ElectorService;
import java.util.List;
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
@RequestMapping("/api/v1/elector")
@Slf4j
public class ElectorController {

  @Autowired
  private ElectorService electorService;

  @GetMapping(path = "{elector_id}")
  public ResponseEntity<NewElector> getElector(@PathVariable("elector_id") long electorId) {
    var elector = electorService.getElector(electorId);

    if (elector == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    return ResponseEntity.ok(elector);
  }

  @GetMapping
  public ResponseEntity<List<NewElector>> getElectors() {
    var electors = electorService.getElectors();
    return ResponseEntity.ok(electors);
  }

  @DeleteMapping(path = "{elector_id}")
  public ResponseEntity<Void> deleteElector(@PathVariable("elector_id") long id) {
    try {
      electorService.deleteElector(id);
    } catch (Exception e) {
      log.error("Elector {} not found", id, e);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return ResponseEntity.ok().build();
  }

  // https://reflectoring.io/bean-validation-with-spring-boot/
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> newElector(
    @Valid @RequestBody NewElector elector,
    BindingResult bindingResult
  ) {
    log.debug("[New Elector] -  {}", elector);
    if (bindingResult.hasErrors()) {
      log.debug("[New Elector] - Validation error: {}", bindingResult.getAllErrors());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    try {
      electorService.newElector(elector);
    } catch (Exception e) {
      log.error("[New Elector] - Error:  ", e);
      throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
    }

    return ResponseEntity.ok().build();
  }
}
