CREATE TABLE IF NOT EXISTS stock (
    id bigint not null primary key,
    symbol VARCHAR(10) NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    timestamp TIMESTAMP NOT NULL
);