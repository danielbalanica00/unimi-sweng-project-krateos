package com.simpolab.server_main.security.dao;

import com.simpolab.server_main.security.domain.User;

public interface UserLoginDAO {
    User findByUsername(String username);
}
