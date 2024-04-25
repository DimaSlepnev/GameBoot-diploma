package com.dmytro.gameboot.dto;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Component
public class EmailValidator implements Predicate<String> {
    @Override
    public boolean test(String s) {
        // TODO: Regex to validate email
        return true;
    }
}
