package com.dmytro.gameboot.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginRequest {

    private String username;
    private String password;
}
