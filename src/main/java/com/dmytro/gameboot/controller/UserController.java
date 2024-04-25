package com.dmytro.gameboot.controller;

import com.dmytro.gameboot.domain.User;
import com.dmytro.gameboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
