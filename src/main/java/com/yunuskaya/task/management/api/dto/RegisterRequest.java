package com.yunuskaya.task.management.api.dto;

import com.yunuskaya.task.management.api.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "Username cannot be empty")
    private String username;

    @Email(message = "Email must be valid")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 5, max = 20, message = "Password must be between 5 and 20 characters")
    private String password;
    private Role role;
}
