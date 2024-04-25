package com.dmytro.gameboot.controller.restController;

import com.dmytro.gameboot.domain.Game;
import com.dmytro.gameboot.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/game-boot/game")
public class SearchGame {

    private final GameService gameService;

    @GetMapping("/search")
    private ResponseEntity<List<Game>> searchGame(@RequestParam("name") String name) {
        return ResponseEntity.ok(gameService.findByNameContaining(name));
    }
}
