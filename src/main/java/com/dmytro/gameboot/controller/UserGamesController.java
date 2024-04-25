package com.dmytro.gameboot.controller;

import com.dmytro.gameboot.domain.Game;
import com.dmytro.gameboot.domain.User;
import com.dmytro.gameboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/game-boot/user/games")
public class UserGamesController {
    private final UserService userService;

    @GetMapping
    public String getAllGamesOfUser(Model model){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Game> games = userService.findUserById(user.getUserId()).getGames();
        model.addAttribute("games", games);
        return "userGames";
    }
}
