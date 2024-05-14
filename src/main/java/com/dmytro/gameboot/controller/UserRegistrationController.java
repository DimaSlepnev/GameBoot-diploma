package com.dmytro.gameboot.controller;

import com.dmytro.gameboot.domain.User;
import com.dmytro.gameboot.dto.RegistrationRequest;
import com.dmytro.gameboot.service.PasswordRecoveryService;
import com.dmytro.gameboot.service.RegistrationService;
import com.dmytro.gameboot.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@Controller
@RequestMapping("/game-boot/registration")
@RequiredArgsConstructor
public class UserRegistrationController {

    private final RegistrationService registrationService;

    private final PasswordRecoveryService passwordRecoveryService;

    private final UserService userService;

    @GetMapping
    public String register(Model model){
        model.addAttribute("registerBody", new RegistrationRequest());
        return "registration";
    }

    @PostMapping
    public String register(@ModelAttribute("registerBody") @Valid RegistrationRequest request,
                           BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()){
            return "registration";
        }
        try {
            registrationService.register(request);
            return "login";
        } catch (IllegalStateException e) {
            model.addAttribute("userNameAlreadyTaken", 1);
        } catch (DataIntegrityViolationException ex){
            model.addAttribute("emailAlreadyTaken", 1);
        }
        return "registration";
    }

    @GetMapping(path = "/confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(Model model) {
        return "forgotPassword";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@ModelAttribute("username") String username, Model model) {
        User user = passwordRecoveryService.sendRecoveryTokenToUser(username);
        String maskedEmail = maskEmail(user.getEmail());
        model.addAttribute("userId", user.getUserId());
        model.addAttribute("maskedEmail", maskedEmail);
        return "forgotPasswordTokenVerification";
    }

    @PostMapping("/verify-token")
    public String verifyToken(@ModelAttribute("token") String token, @RequestParam("userId") Long userId, Model model) {
        passwordRecoveryService.verifyToken(token);
        model.addAttribute("userId", userId);
        return "passwordRecovery";
    }

    @PostMapping("/recovery")
    public String recoverPassword(@RequestParam("userId") Long userId, @ModelAttribute("password") String password, Model model) {
        passwordRecoveryService.recoverUsersPassword(userService.findUserById(userId), password);
        model.addAttribute("passwordRecovery", 1);
        return "login";
    }

    private String maskEmail(String email){
        int atIndex = email.indexOf('@');

        String local = email.substring(0, atIndex);
        String domain = email.substring(atIndex);
        char firstLetter = local.charAt(0);
        StringBuilder maskedLocal = new StringBuilder();
        maskedLocal.append(firstLetter);
        maskedLocal.append("*".repeat(local.length() - 1));
        return maskedLocal.toString() + domain;
    }
}
