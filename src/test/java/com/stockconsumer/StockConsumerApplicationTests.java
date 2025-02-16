package com.stockconsumer;


import com.stockconsumer.model.entity.Stock;
import com.stockconsumer.service.StockService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StockConsumerApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StockService stockService;

    /**
     * Test application context loads without issues.
     */
    @Test
    void contextLoads() {
        ApplicationContext context = SpringApplication.run(StockConsumerApplication.class);
        assertNotNull(context.getBean(StockService.class), "StockService bean should be loaded in the context");
    }

    /**
     * Test GET /stocks endpoint for retrieving all stocks.
     */
    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void testGetAllStocks() throws Exception {
        Stock stock1 = new Stock(1L, "AAPL", 150.0, LocalDateTime.now());
        Stock stock2 = new Stock(2L, "MSFT", 320.5, LocalDateTime.now());

        Mockito.when(stockService.getAllStocks()).thenReturn(List.of(stock1, stock2));

        mockMvc.perform(get("/stocks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].symbol").value("AAPL"))
                .andExpect(jsonPath("$[1].symbol").value("MSFT"));
    }

    /**
     * Test GET /stocks/symbol/{symbol} endpoint for retrieving stock by symbol.
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