package com.simpolab.server_main.elector.api;

import com.simpolab.server_main.elector.domain.Elector;
import com.simpolab.server_main.elector.services.ElectorService;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Slf4j
public class ElectorController {

  @Autowired
  private ElectorService electorService;

  @GetMapping(path = "{elector_id}")
  public ResponseEntity<Map<String, String>> getElector(
    @PathVariable("elector_id") Long electorId
  ) {
    var elector = electorService.getElector(electorId);

    if (elector == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    return ResponseEntity.ok(elector.toFlatMap());
  }

  @GetMapping
  public ResponseEntity<List<Map<String, String>>> getElectors() {
    var electors = electorService.getElectors();

    var result = electors.stream().map(Elector::toFlatMap).toList();

    return ResponseEntity.ok(result);
  }

  @DeleteMapping(path = "{elector_id}")
  public ResponseEntity<Void> deleteElector(@PathVariable("elector_id") Long id) {
    try {
      electorService.deleteElector(id);
    } catch (Exception e) {
      log.error("User {} not found", id, e);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return null;
  }

  // https://reflectoring.io/bean-validation-with-spring-boot/
  @PostMapping(
    path = "",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Void> newElector(
    @Valid @RequestBody Elector elector,
    BindingResult bindingResult
  ) {
    if (bindingResult.hasErrors()) {
      log.warn("Error: {}", bindingResult.getAllErrors());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    try {
      electorService.newElector(elector);
    } catch (Exception e) {
      log.error("Error ---> ", e);
      throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
    }
    return null;
  }
}
