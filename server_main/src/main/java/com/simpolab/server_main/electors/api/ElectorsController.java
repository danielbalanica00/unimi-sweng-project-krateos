package com.simpolab.server_main.electors.api;


import com.simpolab.server_main.electors.domain.Elector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/elector")
@RequiredArgsConstructor
@Slf4j
public class ElectorsController {
    @GetMapping(path="{elector_id}")
    public ResponseEntity<Elector> getElector(@PathVariable("elector_id") Integer electorId) {
        log.info("Received elector id: {}", electorId);
        return ResponseEntity.ok(new Elector());
    }
}
