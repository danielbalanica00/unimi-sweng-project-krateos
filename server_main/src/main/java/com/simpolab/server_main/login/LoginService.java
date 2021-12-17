package com.simpolab.server_main.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    private final LoginDAS loginDAS;

    public LoginService(LoginDAS loginDAS) {
        this.loginDAS = loginDAS;
    }

    public boolean login(String username, String password){
        return loginDAS.login(username, password);
    }
}

