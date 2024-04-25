package com.dmytro.gameboot.service;

import com.dmytro.gameboot.domain.User;
import com.dmytro.gameboot.email.EmailSender;
import com.dmytro.gameboot.service.token.ConfirmationToken;
import com.dmytro.gameboot.service.token.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordRecoveryService {

    private final UserService userService;

    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;

    private final PasswordEncoder encoder;

    public User sendRecoveryTokenToUser(String username) {
        User user = userService.findUserByUserName(username);
        ConfirmationToken token = createRecoveryToken(user);
        confirmationTokenService.saveConfirmationToken(token);
        String subject = "Password Recovery";
        Context context = new Context();
        context.setVariable("subject", subject);
        context.setVariable("name", user.getUsername());
        context.setVariable("token", token.getToken());
        emailSender.send(user.getEmail(),
                subject,
                "passwordRecoveryEmail",
                context);
        return user;
    }

    private ConfirmationToken createRecoveryToken(User user) {
        String uuid = UUID.randomUUID().toString();
        String token = uuid.substring(uuid.length() - 12);
        return ConfirmationToken.builder()
                .token(token)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
    }

    public void verifyToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));
        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("token already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiredAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
    }

    @Transactional
    public void recoverUsersPassword(User user, String password) {
        User oldUser = userService.findUserById(user.getUserId());
        oldUser.setPassword(encoder.encode(password));
        userService.save(user);
    }
}
