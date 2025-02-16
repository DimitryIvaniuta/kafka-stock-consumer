package com.stockconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Stock Consumer microservice application.
 * <p>
 * This service listens to Kafka topics for stock price updates and processes them,
 * storing the data into the PostgreSQL database.
 * <p>
 * The application uses:
 * <ul>
 *     <li>Spring Boot for microservice development</li>
 *     <li>Spring Kafka for consuming messages from Kafka</li>
 * PostgreSQL as the database for persisting stock information</li>
 */
@SpringBootApplication
public class StockConsumerApplication {

    /**
     * Main method for starting the Stock Consumer microservice.
     * <p>
     * This method launches the Spring Boot application by invoking
     * {@link SpringApplication#run(Class, String...)}.
     *
     * @param args command-line arguments passed during application startup
     */
    public static void main(final String[] args) {
        SpringApplication.run(StockConsumerApplication.class, args);
    }

}
