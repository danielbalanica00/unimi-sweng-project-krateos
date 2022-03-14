package com.simpolab.server_main.users.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppUser {
    private Long id;
    private String username;
    private String password;
    private String role;
}
