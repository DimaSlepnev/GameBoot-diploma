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

    @NotEmpty(message = "{username.not.empty}")
    @Size(min = 2, max = 15, message = "{username.size}")
    private String username;

    @NotEmpty(message = "{email.not.empty}")
    @Email(message = "{not.email}")
    private String email;

    @NotEmpty(message = "")
    @Size(min = 8, message = "")
    private String password;
}
