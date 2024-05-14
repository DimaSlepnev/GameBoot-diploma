package com.dmytro.gameboot.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationRequest {

    @NotEmpty(message = "Username must be not empty")
    @Size(min = 2, max = 15, message = "Username must be between 2 and 15 characters")
    private String username;

    @NotEmpty(message = "Email must be not empty")
    @Email(message = "This is not email")
    private String email;

    @NotEmpty(message = "Password must be not empty")
    @Size(min = 8, message = "Password must contain minimum 8 characters")
    private String password;
}
