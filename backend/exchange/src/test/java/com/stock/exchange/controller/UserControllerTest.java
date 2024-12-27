package com.stock.exchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.exchange.model.LoginUser;
import com.stock.exchange.model.RegisterUser;
import com.stock.exchange.repository.model.User;
import com.stock.exchange.service.AuthenticationService;
import com.stock.exchange.service.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserDetailsService userDetailsService;


    @Test
    @DisplayName("Testing signing up a user")
    public void givenUserDetailsToSignUpExpectedCorrectResponse() throws Exception {
        //given
        RegisterUser registerUser = new RegisterUser();
        registerUser.setName("John Doe");
        registerUser.setEmailId("john.doe@test.com");
        registerUser.setPassword("password");

        User user = new User();
        user.setName("John Doe");
        user.setEmailId("john.doe@test.com");

        //when
        when(authenticationService.signup(any(RegisterUser.class))).thenReturn(user);

        //then
        mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registerUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.emailId").value("john.doe@test.com"));
    }

    @Test
    @DisplayName("Testing logging in a user")
    public void givenUserDetailsToLogInExpectedCorrectResponse() throws Exception {
        //given
        LoginUser loginUser = new LoginUser();
        User user = new User();
        user.setName("John Doe");
        user.setEmailId("john.doe@test.com");

        //when
        when(authenticationService.authenticate(any(LoginUser.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("dummyToken");

        //then
        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }
}
