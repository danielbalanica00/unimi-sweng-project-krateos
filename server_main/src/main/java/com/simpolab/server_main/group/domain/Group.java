package com.simpolab.server_main.group.domain;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class Group {

  @Min(0)
  private Long id;

  @NotBlank
  private String name;
}
