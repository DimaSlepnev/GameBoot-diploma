package com.dmytro.gameboot.controller;

import com.dmytro.gameboot.domain.Genre;
import com.dmytro.gameboot.service.GameService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/game-boot/main")
@RequiredArgsConstructor
public class MainController {

    private final GameService gameService;

    private HttpSession session;

    @GetMapping
    public String main(Model model) {
        model.addAttribute("games", gameService.findAll());
        model.addAttribute("text", "All games");
        session.setAttribute("genres", Genre.values());
        return "main";
    }
}
