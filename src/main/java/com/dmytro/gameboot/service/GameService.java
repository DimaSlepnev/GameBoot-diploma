package com.dmytro.gameboot.service;

import com.dmytro.gameboot.domain.Game;
import com.dmytro.gameboot.domain.GameDetail;
import com.dmytro.gameboot.domain.Genre;
import com.dmytro.gameboot.dto.GameRequest;
import com.dmytro.gameboot.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;

    private final GameDetailService gameDetailService;

    public void save(Game game) {
        gameRepository.save(game);
    }

    public List<Game> findByNameContaining(String name) {
        return gameRepository.getGameByNameContainingIgnoreCase(name);
    }

    public List<Game> findAll() {
        return gameRepository.findAll();
    }

    public Game findGameById(Long gameId) {
        return gameRepository.findById(gameId).orElseThrow(() -> new IllegalStateException("Not_Found"));
    }

    public List<Game> getGamesByGenre(Genre genre) {
        return gameRepository.getGamesByGenre(genre);
    }

    public Page<Game> getAllGamesWithDetails(Pageable pageable) {
        return gameRepository.findAllGamesWithGameDetails(pageable);
    }

    public Page<Game> getGameByNameWithGameDetails(String name, Pageable pageable){
        return gameRepository.getGameByNameWithGameDetails(name, pageable);
    }

    public void save(GameRequest gameRequest) {
        GameDetail gameDetail = GameDetail.builder()
                .price(gameRequest.getPrice())
                .count(gameRequest.getCount())
                .yearOfProduction(gameRequest.getYearOfProduction())
                .build();
        Game game = Game.builder()
                .name(gameRequest.getName())
                .genres(gameRequest.getGenres())
                .gameDetail(gameDetail)
                .build();
        gameDetailService.save(gameDetail);
        gameDetail.setGame(game);
        gameRepository.save(game);
        gameDetailService.save(gameDetail);
    }
}
