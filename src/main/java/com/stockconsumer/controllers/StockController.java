package com.stockconsumer.controllers;

import com.stockconsumer.model.entity.Stock;
import com.stockconsumer.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for managing stock operations.
 * Provides endpoints to retrieve, create, update, and delete stock information.
 */
@RestController
@RequestMapping("/stocks")
@RequiredArgsConstructor
public class StockController {

    /**
     * The service responsible for handling business logic
     * related to stock operations, such as retrieval, creation,
     * updating, and deletion of stocks.
     */
    private final StockService stockService;

    /**
     * Retrieves all stocks from the database.
     *
     * @return a list of all {@link Stock} objects.
     */
    @GetMapping
    public ResponseEntity<List<Stock>> getAllStocks() {
        List<Stock> stocks = stockService.getAllStocks();
        return ResponseEntity.ok(stocks);
    }

    /**
     * Retrieves a stock by its ID.
     *
     * @param id the unique identifier of the stock.
     * @return the {@link Stock} object if found, or 404 Not Found if not.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Stock> getStockById(@PathVariable final Long id) {
        return stockService.getStockById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Creates a new stock.
     *
     * @param stock the {@link Stock} object to be created.
     * @return the created {@link Stock} object with status 201 Created.
     */
    @PostMapping
    public ResponseEntity<Stock> createStock(@RequestBody final Stock stock) {
        Stock createdStock = stockService.createStock(stock);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStock);
    }

    /**
     * Updates an existing stock by its ID.
     *
     * @param id the unique identifier of the stock to be updated.
     * @param stock the updated {@link Stock} object.
     * @return the updated {@link Stock} object, or 404 Not Found if the stock does not exist.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Stock> updateStock(@PathVariable final Long id, @RequestBody final Stock stock) {
        return stockService.updateStock(id, stock)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes a stock by its ID.
     *
     * @param id the unique identifier of the stock to be deleted.
     * @return a 204 No Content response if successful, or 404 Not Found if the stock does not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable final Long id) {
        if (stockService.deleteStock(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

