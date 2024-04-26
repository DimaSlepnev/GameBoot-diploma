package com.dmytro.gameboot.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long gameId;

    private String name;

    @ElementCollection(targetClass = Genre.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<Genre> genres;

    @OneToOne(mappedBy = "game")
    @JsonManagedReference
    private GameDetail gameDetail;

    /*@ManyToMany(mappedBy = "games")
    @JsonBackReference
    private List<User> users;*/

    @OneToMany(mappedBy = "game")
    @JsonManagedReference
    private List<UserGame> users;
}
