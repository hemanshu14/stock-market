package com.stock.exchange.service;

import com.stock.exchange.model.LoginUser;
import com.stock.exchange.model.RegisterUser;
import com.stock.exchange.repository.UserRepository;
import com.stock.exchange.repository.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public User signup(RegisterUser input) {
        User user = new User();
        user.setName(input.getName());
        user.setEmailId(input.getEmailId());
        user.setPassword(passwordEncoder.encode(input.getPassword()));

        return userRepository.save(user);
    }

    public User authenticate(LoginUser input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmailId(),
                        input.getPassword()
                ));

        return userRepository.findByEmailId(input.getEmailId())
                .orElseThrow();
    }
}
