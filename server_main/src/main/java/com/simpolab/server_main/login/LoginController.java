package com.simpolab.server_main.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequestMapping("api/v1/login")
@RestController
public class LoginController {
    @Autowired
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public boolean login(HttpServletRequest req){
        Map<String,String[]> params =  req.getParameterMap();

        for(String key : params.keySet()) {
            System.out.print(key + ": ");
            for (String val : params.get(key)){
                System.out.print(val + ", ");
            }
            System.out.println();
        }

        return loginService.login(params.get("username")[0], params.get("password")[0]);
    }
}
