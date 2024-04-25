package com.dmytro.gameboot.service;

import com.dmytro.gameboot.domain.Role;
import com.dmytro.gameboot.domain.User;
import com.dmytro.gameboot.dto.EmailValidator;
import com.dmytro.gameboot.dto.RegistrationRequest;
import com.dmytro.gameboot.email.EmailSender;
import com.dmytro.gameboot.service.token.ConfirmationToken;
import com.dmytro.gameboot.service.token.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final EmailValidator emailValidator;

    private final UserService userService;
    private final PasswordEncoder encoder;

    private final EmailSender emailSender;

    private final ConfirmationTokenService confirmationTokenService;


    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getUsername());
        if (!isValidEmail) {
            throw new IllegalStateException("Username not valid");
        }
        String pass = encoder.encode(request.getPassword());
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(pass)
                .enabled(false)
                .balance(0.0)
                .role(Role.USER)
                .build();
        String token = userService.singUp(user);
        String link = "http://localhost:8085/game-boot/registration/confirm?token=" + token;
        String subject = "Confirm your email";
        Context context = new Context();
        context.setVariable("subject", subject);
        context.setVariable("name", user.getUsername());
        context.setVariable("link", link);
        emailSender.send(
                user.getEmail(),
                subject,
                "buildEmail",
                context
        );
        return token;
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiredAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        userService.enableUser(
                confirmationToken.getUser().getEmail());
        return "confirmed";
    }
}
