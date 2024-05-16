package com.dmytro.gameboot.controller;

import com.dmytro.gameboot.domain.User;
import com.dmytro.gameboot.domain.UserGame;
import com.dmytro.gameboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/game-boot/user/games")
public class UserGamesController {
    private final UserService userService;

    @GetMapping
    public String getAllGamesOfUser(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<UserGame> games = userService.findUserById(user.getUserId()).getGame();
        List<UserGame> sortedGames = games.stream()
                .sorted((g1, g2) -> g2.getBuyAt().isAfter(g1.getBuyAt()) ? 1 : -1)
                .collect(Collectors.toList());
        model.addAttribute("games", sortedGames);
        return "userGames";
    }
}
