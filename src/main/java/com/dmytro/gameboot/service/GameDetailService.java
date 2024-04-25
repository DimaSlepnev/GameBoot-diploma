package com.dmytro.gameboot.service;

import com.dmytro.gameboot.domain.GameDetail;
import com.dmytro.gameboot.repository.GameDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameDetailService {

    private final GameDetailRepository gameDetailRepository;

    public void save(GameDetail detail){
        gameDetailRepository.save(detail);
    }
}
