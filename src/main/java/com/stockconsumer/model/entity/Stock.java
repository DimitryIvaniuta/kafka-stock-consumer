package com.stockconsumer.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing a stock in the system.
 * <p>
 * Each stock has a unique identifier, a symbol (e.g., "AAPL" for Apple Inc.),
 * and a current price. This class is mapped to the "stocks" table in the database.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock {

    /**
     * Unique identifier for the stock.
     * <p>
     * This field is automatically generated and serves as the primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STOCK_UNIQUE_ID")
    @SequenceGenerator(name = "STOCK_UNIQUE_ID", sequenceName = "STOCK_UNIQUE_ID", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Symbol representing the stock (e.g., "AAPL" for Apple Inc.).
     * <p>
     * This field is mandatory and must be unique.
     */
    @Column(name = "symbol", nullable = false, unique = true, length = 10)
    private String symbol;

    /**
     * Current price of the stock.
     * <p>
     * This field represents the latest known price of the stock.
     */
    @Column(name = "price", nullable = false)
    private Double price;

    /**
     * Timestamp indicating when the stock price was last updated.
     * <p>
     * This field stores the date and time of the latest update
     * and provides information about when the price data was recorded.
     */
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

}
