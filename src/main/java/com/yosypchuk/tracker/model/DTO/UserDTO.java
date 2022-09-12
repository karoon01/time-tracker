package com.yosypchuk.tracker.model.DTO;

import com.yosypchuk.tracker.model.entity.Role;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class UserDTO {
    private Long id;

    private Role role;

    @NotBlank(message = "${first.name.not-blank}")
    private String firstName;

    @NotBlank(message = "${last.name.not-blank}")
    private String lastName;

    @NotBlank(message = "${email.not-blank}")
    @Email(message = "${email.valid}")
    private String email;

    @NotBlank(message = "${password.not-blank}")
    @Size(min = 6, message = "${password.length}")
    private String password;
}
