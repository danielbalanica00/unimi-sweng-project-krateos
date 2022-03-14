package com.simpolab.server_main.security.dao;

import com.simpolab.server_main.security.domain.AppUser;

public interface UserLoginDAO {
    AppUser findByUsername(String username);
}
