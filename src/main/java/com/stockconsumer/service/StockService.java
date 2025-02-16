package com.stockconsumer.service;

import com.stockconsumer.model.entity.Stock;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing stock operations.
 * Defines methods for retrieving, creating, updating, and deleting stock data.
 */
public interface StockService {

    /**
     * Retrieves all stocks from the database.
     *
     * @return a list of all {@link Stock} objects.
     */
    List<Stock> getAllStocks();

    /**
     * Retrieves a stock by its unique identifier.
     *
     * @param id the unique identifier of the stock.
     * @return an {@link Optional} containing the {@link Stock} if found, or an empty {@link Optional} if not.
     */
    Optional<Stock> getStockById(Long id);

    /**
     * Fetch list of stocks by its symbol.
     *
     * @param symbol of the stock.
     * @return a list of Stocks.
     */
    public List<Stock> getStocksBySymbol(String symbol);

    /**
     * Creates a new stock and saves it to the database.
     *
     * @param stock the {@link Stock} object to create.
     * @return the created {@link Stock} object.
     */
    Stock createStock(Stock stock);

    /**
     * Updates an existing stock identified by its ID.
     *
     * @param id the unique identifier of the stock to update.
     * @param stock the updated {@link Stock} object.
     * @return an {@link Optional} containing the updated {@link Stock}, or empty if the stock is not found.
     */
    Optional<Stock> updateStock(Long id, Stock stock);

    /**
     * Deletes a stock identified by its unique identifier.
     *
     * @param id the unique identifier of the stock to delete.
     * @return {@code true} if the stock was deleted successfully, {@code false} otherwise.
     */
    boolean deleteStock(Long id);
}
