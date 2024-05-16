package com.dmytro.gameboot.dto;

import com.dmytro.gameboot.domain.Genre;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameRequest {

    private Long gameId;

    @NotEmpty(message = "{name.not.empty}")
    private String name;

    @NotEmpty(message = "{choose.at.least.one}")
    private List<Genre> genres;

    @NotNull(message = "{price.not.empty}")
    @Min(value = 0, message = "{price.not.negative}")
    private Double price;


    @Max(value = 2024, message = "{year.before}")
    @Min(value = 1995, message = "{year.after}")
    private Integer yearOfProduction;

    @NotNull(message = "{count.not.empty}")
    @Min( value = 1, message = "{count.greater.zero}")
    private Integer count;

    private String photoUrl;
}
