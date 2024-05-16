package com.dmytro.gameboot.controller;

import com.dmytro.gameboot.domain.Genre;
import com.dmytro.gameboot.service.GameService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/game-boot/main")
@RequiredArgsConstructor
public class MainController {

    private final GameService gameService;

    private final HttpSession session;

    private final MessageSource messageSource;

    @GetMapping
    public String main(Model model) {
        String allGames = messageSource.getMessage("all.games", null, LocaleContextHolder.getLocale());
        model.addAttribute("games", gameService.findAll());
        model.addAttribute("text", allGames);
        session.setAttribute("genres", Genre.values());
        return "main";
    }
}
