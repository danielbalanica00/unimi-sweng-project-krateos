package com.simpolab.server_main.elector.domain;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class NewElector {

  @Min(0)
  private Long id;

  @NotBlank
  private String username;

  @NotBlank
  private String password;

  @NotBlank
  private String firstName;

  @NotBlank
  private String lastName;

  @Email
  private String email;

  @NotBlank
  @Pattern(regexp = "(?i)manager|elector")
  private String role;

  public NewElector() {
    this.role = "elector";
  }

  public NewElector sanitized() {
    this.password = null;
    this.role = null;
    return this;
  }
}
