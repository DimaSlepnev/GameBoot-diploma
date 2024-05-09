package com.dmytro.gameboot.service;

import com.dmytro.gameboot.domain.Game;
import com.dmytro.gameboot.domain.Role;
import com.dmytro.gameboot.domain.User;
import com.dmytro.gameboot.domain.UserGame;
import com.dmytro.gameboot.email.EmailSender;
import com.dmytro.gameboot.repository.UserRepository;
import com.dmytro.gameboot.service.token.ConfirmationToken;
import com.dmytro.gameboot.service.token.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "User with username %s not found";
    private final UserRepository userRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final GameService gameService;
    private final UserGameService userGameService;
    private final EmailSender emailSender;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, username)));
    }

    public User findUserByUserName(String username) {
        return userRepository.findUserByUsername(username).orElseThrow(() ->
                new IllegalStateException("Username not found"));
    }

    public String singUp(User user) {
        boolean userExist = userRepository.findUserByUsername(user.getUsername()).isPresent();
        if (userExist) {
            throw new IllegalStateException("username already taken");
        }
        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .token(token)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }

    public int enableUser(String email) {
        return userRepository.enableUser(email);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalStateException("User not found"));
    }

    @Transactional
    public void topUpBalance(Double amount, User user) {
        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);
    }

    @Transactional
    public boolean buyGame(Long gameId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Game game = gameService.findGameById(gameId);
        if (game.getGameDetail().getCount() > 0 && user.getBalance() >= game.getGameDetail().getPrice()) {
            String gameCode = UUID.randomUUID().toString();
            user.setBalance(user.getBalance() - game.getGameDetail().getPrice());
            UserGame userGame = UserGame.builder()
                    .gameCode(gameCode)
                    .buyAt(LocalDateTime.now())
                    .gamePrice(game.getGameDetail().getPrice())
                    .build();
            user.getGame().add(userGame);
            game.getGameDetail().setCount(game.getGameDetail().getCount() - 1);
            userGameService.save(userGame);
            userGame.setUser(user);
            userGame.setGame(game);
            userRepository.save(user);
            gameService.save(game);
            userGameService.save(userGame);
            sendGameCodeToEmail(game, user, userGame);
        }
        return true;
    }

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    private void sendGameCodeToEmail(Game game, User user, UserGame userGame) {
        String subject = "Message about purchase " + game.getName();
        Context context = new Context();
        context.setVariable("subject", subject);
        context.setVariable("name", user.getUsername());
        context.setVariable("gameCode", userGame.getGameCode());
        emailSender.send(
                user.getEmail(),
                subject,
                "gamePurchaseEmail",
                context
        );
    }

    @Transactional
    public void promoteUserToAdmin(String email) {
        User user = userRepository.findUserByEmail(email);
        user.setRole(Role.ADMIN);
        userRepository.save(user);
        sendMessageAboutPromotionToUser(user);
    }

    private void sendMessageAboutPromotionToUser(User user) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String subject = "You have been promoted to admin!";
        Context context = new Context();
        context.setVariable("subject", subject);
        context.setVariable("userName", user.getUsername());
        context.setVariable("promotedBy", currentUser.getUsername());
        emailSender.send(
                user.getEmail(),
                subject,
                "promoteToAdminEmail",
                context
        );
    }

    public Page<User> findAllByUsernameOrEmailContaining(String criteria, Pageable pageable) {
        return userRepository.findAllByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(criteria, pageable);
    }
}
