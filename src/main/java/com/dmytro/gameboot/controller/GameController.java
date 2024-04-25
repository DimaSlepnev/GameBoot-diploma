package com.dmytro.gameboot.controller;

import com.dmytro.gameboot.domain.Game;
import com.dmytro.gameboot.domain.Genre;
import com.dmytro.gameboot.domain.User;
import com.dmytro.gameboot.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/game-boot/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @GetMapping("/{gameId}")
    public String getGameById(@PathVariable("gameId") Long gameId, Model model) {
        Game game = gameService.findGameById(gameId);
        var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("balance", user.getBalance());
        model.addAttribute("game", game);
        return "gamePage";
    }

    @GetMapping("/get/{genre}")
    public String getGamesByGenre(@PathVariable("genre") Genre genre, Model model){
        /*Genre genre = Genre.valueOf(genreStr);*/
        List<Game> games = gameService.getGamesByGenre(genre);
        model.addAttribute("games", games);
        model.addAttribute("text", genre.getValue());
        return "main";
    }
}
