package com.stock.exchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.exchange.repository.StockRepository;
import com.stock.exchange.repository.model.Stock;
import com.stock.exchange.service.AuthenticationService;
import com.stock.exchange.service.JwtService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.stock.exchange.config.Constants.STOCK_NOT_FOUND;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StockControllerTest {
    @MockBean
    private JwtService jwtService;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private StockRepository stockRepository;


    @Test
    @DisplayName("Testing creating a stock with Bearer token")
    public void givenStockDetailsToCreateStockExpectedCorrectResponse() throws Exception {
        //given
        Stock stock = new Stock();
        stock.setName("Facebook");
        stock.setDescription("facebook co.");
        stock.setCurrentPrice(24.8);

        //when
        when(jwtService.extractUsername(anyString())).thenReturn("john.doe@test.com");
        when(userDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new org.springframework.security.core.userdetails.User("john.doe@test.com",
                        "password",
                        new ArrayList<>()));
        when(jwtService.isTokenValid(anyString(), any())).thenReturn(true);
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        //then
        mockMvc.perform(post("/api/v1/stocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer dummyToken")
                        .content(new ObjectMapper().writeValueAsString(stock)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Facebook"))
                .andExpect(jsonPath("$.description").value("facebook co."))
                .andExpect(jsonPath("$.currentPrice").value(24.8));
    }

    @Test
    @DisplayName("Testing creating a stock without Bearer token")
    public void givenStockDetailsWithoutTokenExpectedUnauthorizedResponse() throws Exception {
        //given
        Stock stock = new Stock();
        stock.setName("Facebook");
        stock.setDescription("facebook co.");
        stock.setCurrentPrice(24.8);

        //then
        mockMvc.perform(post("/api/v1/stocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(stock)))
                .andExpect(status().isForbidden());
    }


    @Test
    @DisplayName("Testing getting stocks from DB with Bearer token")
    public void gettingStocksFromDbExpectedCorrectResponse() throws Exception {
        //given
        Stock stock = new Stock();
        stock.setName("Facebook");
        stock.setDescription("facebook co.");
        stock.setCurrentPrice(24.8);

        //when
        when(jwtService.extractUsername(anyString())).thenReturn("john.doe@test.com");
        when(userDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new org.springframework.security.core.userdetails.User("john.doe@test.com",
                        "password",
                        new ArrayList<>()));
        when(jwtService.isTokenValid(anyString(), any())).thenReturn(true);
        when(stockRepository.findAll()).thenReturn(List.of(stock));

        //then
        mockMvc.perform(get("/api/v1/stocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer dummyToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("Testing updating a stock's price with Bearer token")
    public void givenStockDetailsToUpdateStockPriceExpectedCorrectResponse() throws Exception {
        //given
        Stock stock = new Stock();
        stock.setId(1);
        stock.setCurrentPrice(22.8);

        //when
        when(jwtService.extractUsername(anyString())).thenReturn("john.doe@test.com");
        when(userDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new org.springframework.security.core.userdetails.User("john.doe@test.com",
                        "password",
                        new ArrayList<>()));
        when(jwtService.isTokenValid(anyString(), any())).thenReturn(true);
        when(stockRepository.findById(anyInt())).thenReturn(Optional.of(stock));
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        //then
        mockMvc.perform(put("/api/v1/stocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer dummyToken")
                        .content(new ObjectMapper().writeValueAsString(stock)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Testing updating a stock's price with Bearer token getting exception")
    public void givenStockDetailsToUpdateStockPriceExpectedException() throws Exception {
        //given
        Stock stock = new Stock();
        stock.setId(1);
        stock.setCurrentPrice(22.8);

        //when
        when(jwtService.extractUsername(anyString())).thenReturn("john.doe@test.com");
        when(userDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new org.springframework.security.core.userdetails.User("john.doe@test.com",
                        "password",
                        new ArrayList<>()));
        when(jwtService.isTokenValid(anyString(), any())).thenReturn(true);
        when(stockRepository.findById(anyInt())).thenThrow(new EntityNotFoundException(STOCK_NOT_FOUND));
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        //then
        mockMvc.perform(put("/api/v1/stocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer dummyToken")
                        .content(new ObjectMapper().writeValueAsString(stock)))
                .andExpect(status().isNotFound());
    }
}
