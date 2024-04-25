package com.dmytro.gameboot.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import jakarta.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Double price;

    private Integer yearOfProduction;

    private Integer count;

    @OneToOne
    @JoinColumn(name="game_id")
    @JsonBackReference
    private Game game;
}
