package com.dmytro.gameboot.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopUpRequest {
    private Double amount;
}
