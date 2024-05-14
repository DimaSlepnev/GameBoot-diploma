package com.dmytro.gameboot.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Genre {

    ACTION("Action"),
    STRATEGY("Strategy"),
    ROLE_PLAYING("Role playing"),
    ADVENTURE("Adventure"),
    SIMULATOR("Simulator"),
    SPORTS_AND_RACING("Sport and racing"),
    Other("Other");

    private final String value;
}
