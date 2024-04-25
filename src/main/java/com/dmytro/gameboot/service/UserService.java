package com.dmytro.gameboot.service;

import com.dmytro.gameboot.domain.User;
import com.dmytro.gameboot.email.EmailSender;
import com.dmytro.gameboot.repository.UserRepository;
import com.dmytro.gameboot.service.token.ConfirmationToken;
import com.dmytro.gameboot.service.token.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "User with username %s not found";
    private final UserRepository userRepository;
    private final ConfirmationTokenService confirmationTokenService;


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
}
