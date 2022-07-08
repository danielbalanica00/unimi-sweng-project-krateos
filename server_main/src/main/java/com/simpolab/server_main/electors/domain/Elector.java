package com.simpolab.server_main.electors.domain;

import com.simpolab.server_main.user_authentication.domain.AppUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Elector {
    private AppUser user;
    private String firstName;
    private String lastName;
    private String email;

    @Override
    public String toString() {
        return "Ciao sono un elector";
    }
}
