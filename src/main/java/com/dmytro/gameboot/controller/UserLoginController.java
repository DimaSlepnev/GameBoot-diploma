package com.dmytro.gameboot.controller;

import com.dmytro.gameboot.dto.RegistrationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/game-boot")
public class UserLoginController {

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("registerBody", new RegistrationRequest());
        return "login";
    }

    @GetMapping()
    public String redirectToMain(){
        return "redirect:/game-boot/main";
    }
}
