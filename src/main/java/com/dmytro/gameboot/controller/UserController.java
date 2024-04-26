package com.dmytro.gameboot.controller;

import com.dmytro.gameboot.domain.User;
import com.dmytro.gameboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/game-boot/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/account")
    public String getUserAccount(Model model){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        return "userAccount";
    }

    @PostMapping("/top-up")
    public String topUpBalance(@RequestParam Double amount, Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.topUpBalance(amount, user);
        model.addAttribute("user", user);
        return "redirect:/game-boot/user/account";
    }

    @PostMapping("/buy-game")
    public String buyGame(@RequestParam("gameId") Long gameId, Model model){
        userService.buyGame(gameId);
        return "redirect:/game-boot/user/games";
    }
}
