package com.dmytro.gameboot.service;

import com.dmytro.gameboot.domain.UserGame;
import com.dmytro.gameboot.repository.UserGameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserGameService {

    final private UserGameRepository userGameRepository;

    public void save(UserGame userGame){
        userGameRepository.save(userGame);
    }
}
