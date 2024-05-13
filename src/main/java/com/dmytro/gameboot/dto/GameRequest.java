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

    @NotEmpty(message = "Name can't be empty")
    private String name;

    private List<Genre> genres;

    @NotNull(message = "Price can't be empty")
    @Min(value = 0, message = "Price cant be negative")
    private Double price;


    @Max(value = 2024, message = "Year must be before 2024")
    @Min(value = 1995, message = "Year must be after 1995")
    private Integer yearOfProduction;

    @NotNull(message = "Your must specified count")
    @Min( value = 1, message = "Count must be greater 0")
    private Integer count;
}
