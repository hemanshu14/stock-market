package com.stock.exchange.service;

import com.stock.exchange.model.LoginUser;
import com.stock.exchange.model.RegisterUser;
import com.stock.exchange.repository.UserRepository;
import com.stock.exchange.repository.model.User;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.stock.exchange.config.Constants.USER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    @DisplayName("testing signing up a user")
    void givenUserDetailsToSignUpExpectedCorrectResponse() {
        //given
        RegisterUser registerUser = new RegisterUser();
        registerUser.setName("John Doe");
        registerUser.setEmailId("john.doe@example.com");
        registerUser.setPassword("password");

        User user = new User();
        user.setName("John Doe");
        user.setEmailId("john.doe@example.com");
        user.setPassword("encodedPassword");

        //when
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = authenticationService.signup(registerUser);

        //then
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmailId());
        assertEquals("encodedPassword", result.getPassword());

        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("testing authenticating a user")
    void givenUserDetailsToAuthenticateExpectedCorrectResponse() {
        //given
        LoginUser loginUser = new LoginUser();
        loginUser.setEmailId("john.doe@example.com");
        loginUser.setPassword("password");

        User user = new User();
        user.setName("John Doe");
        user.setEmailId("john.doe@example.com");
        user.setPassword("encodedPassword");

        //when
        when(userRepository.findByEmailId(anyString())).thenReturn(Optional.of(user));

        User result = authenticationService.authenticate(loginUser);

        //then
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmailId());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByEmailId("john.doe@example.com");
    }

    @Test
    @DisplayName("testing user bot found in db exception")
    void givenUserDetailsToAuthenticateExpectedUserNotFound() {
        LoginUser loginUser = new LoginUser();
        loginUser.setEmailId("john.doe@example.com");
        loginUser.setPassword("password");

        when(userRepository.findByEmailId(anyString())).thenThrow(new EntityNotFoundException(USER_NOT_FOUND));

        assertThrows(EntityNotFoundException.class, () -> authenticationService.authenticate(loginUser));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByEmailId("john.doe@example.com");
    }
}
