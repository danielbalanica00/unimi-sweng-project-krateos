package com.simpolab.server_main.users.dao;

import com.simpolab.server_main.users.domain.AppUser;

public interface UserLoginDAO {
    AppUser findByUsername(String username);
}
