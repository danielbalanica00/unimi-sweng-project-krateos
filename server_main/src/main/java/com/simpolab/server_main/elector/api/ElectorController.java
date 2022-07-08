package com.simpolab.server_main.elector.api;

import com.simpolab.server_main.elector.domain.Elector;
import com.simpolab.server_main.elector.services.ElectorService;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
  @PreAuthorize("hasAuthority('MANAGER')")
  public ResponseEntity<Map<String, String>> getElector(
    @PathVariable("elector_id") Long electorId
  ) {
    var elector = electorService.getElectorById(electorId);
    return ResponseEntity.ok(elector.toFlatMap());
  }

  // https://reflectoring.io/bean-validation-with-spring-boot/
  @PostMapping(
    path = "",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @PreAuthorize("hasAuthority('MANAGER')")
  public ResponseEntity<Void> newElector(
    @Valid @RequestBody Elector elector,
    BindingResult bindingResult
  ) {
    if (bindingResult.hasErrors()) {
      log.warn("Error: {}", bindingResult.getAllErrors());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    log.info(
      "authorities: {}",
      SecurityContextHolder.getContext().getAuthentication().getAuthorities()
    );

    try {
      electorService.createNewElector(elector);
    } catch (Exception e) {
      log.error("Error ---> ", e);
      throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
    }
    return null;
  }
}
