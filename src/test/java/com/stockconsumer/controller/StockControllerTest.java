package com.stockconsumer.controller;

import com.stockconsumer.controllers.StockController;
import com.stockconsumer.model.entity.Stock;
import com.stockconsumer.service.StockService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for StockController with mock security and mock service.
 */
@WebMvcTest(StockController.class)
@TestPropertySource(locations = "classpath:application-test.yml")
class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StockService stockService;

    /**
     * Test GET /stocks with a mock authenticated user.
     * Expects 200 OK when the user is authorized.
     */
    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void testGetAllStocks() throws Exception {
        List<Stock> mockStocks = List.of(
                new Stock(1L, "AAPL", 150.0, LocalDateTime.now()),
                new Stock(2L, "MSFT", 320.5, LocalDateTime.now())
        );
        Mockito.when(stockService.getAllStocks()).thenReturn(mockStocks);

        mockMvc.perform(get("/stocks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].symbol").value("AAPL"));
    }

    /**
     * Test GET /stocks/symbol/{symbol} with a mock authenticated user.
     * Expects 200 OK and stock with the correct symbol.
     */
    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void testGetStocksBySymbol() throws Exception {
        Stock mockStock = new Stock(1L, "AAPL", 150.0, LocalDateTime.now());
        Mockito.when(stockService.getStocksBySymbol("AAPL")).thenReturn(List.of(mockStock));

        mockMvc.perform(get("/stocks/symbol/AAPL")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].symbol").value("AAPL"));
    }

}
