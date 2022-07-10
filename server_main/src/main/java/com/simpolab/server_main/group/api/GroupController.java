package com.simpolab.server_main.group.api;

import com.simpolab.server_main.elector.services.ElectorService;
import com.simpolab.server_main.group.services.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/group")
@Slf4j
public class GroupController {

  @Autowired
  private GroupService groupService;
}
