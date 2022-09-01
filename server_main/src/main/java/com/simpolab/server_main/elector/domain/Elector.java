package com.simpolab.server_main.elector.domain;

import com.simpolab.server_main.user_authentication.domain.AppUser;
import java.util.Map;
import java.util.TreeMap;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class Elector {

  @Valid
  private AppUser user;

  @NotBlank
  private String firstName;

  @NotBlank
  private String lastName;

  @Email
  private String email;

  public Map<String, String> toFlatMap() {
    Map<String, String> map = new TreeMap<>();
    map.put("id", user.getId().toString());
    map.put("username", user.getUsername());
    map.put("firstName", firstName);
    map.put("lastName", lastName);
    map.put("email", email);
    return map;
  }
}
