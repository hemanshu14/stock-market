package com.stock.exchange.controller;

import com.stock.exchange.model.LoginResponse;
import com.stock.exchange.model.LoginUser;
import com.stock.exchange.model.RegisterUser;
import com.stock.exchange.repository.model.User;
import com.stock.exchange.service.AuthenticationService;
import com.stock.exchange.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUser registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUser loginUserDto, HttpServletResponse response) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);


        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setName(authenticatedUser.getName());
        loginResponse.setToken(jwtToken);

        return ResponseEntity.ok(loginResponse);
    }
}
