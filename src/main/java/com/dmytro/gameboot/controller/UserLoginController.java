package com.dmytro.gameboot.controller;

import com.dmytro.gameboot.domain.Genre;
import com.dmytro.gameboot.dto.RegistrationRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/game-boot")
@RequiredArgsConstructor
public class UserLoginController {

    private final HttpSession session;


    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("registerBody", new RegistrationRequest());
        session.setAttribute("genres", Genre.values());
        return "login";
    }

    @GetMapping()
    public String redirectToMain(){
        session.setAttribute("genres", Genre.values());
        return "redirect:/game-boot/main";
    }
}
