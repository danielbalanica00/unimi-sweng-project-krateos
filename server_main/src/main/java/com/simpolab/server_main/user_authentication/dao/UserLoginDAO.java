package com.simpolab.server_main.user_authentication.dao;

import com.simpolab.server_main.user_authentication.domain.AppUser;

public interface UserLoginDAO {
    AppUser findByUsername(String username);
}
