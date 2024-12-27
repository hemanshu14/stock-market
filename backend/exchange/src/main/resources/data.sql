INSERT INTO STOCKEXCHANGE (id, name, description, liveinmarket) VALUES (1,'NASDAQ', 'NASDAQ', 1);
INSERT INTO STOCKEXCHANGE (id, name, description, liveinmarket) VALUES (2,'BSE', 'Bombay Stock Exchange', 1);
INSERT INTO STOCKEXCHANGE (id, name, description, liveinmarket) VALUES (3,'SSE', 'Shangai Stock Exchange', 1);
INSERT INTO STOCKEXCHANGE (id, name, description, liveinmarket) VALUES (4,'NSE', 'National Stock Exchange', 0);

INSERT INTO STOCK (id, name, description, current_price, last_update) VALUES (1,'Facebook', 'Facebook co.', 10.6, '2024-12-24 11:43:47.682033');
INSERT INTO STOCK (id, name, description, current_price, last_update) VALUES (2,'Samsung', 'Samsung co.', 12.4, '2024-12-24 11:43:47.682033');
INSERT INTO STOCK (id, name, description, current_price, last_update) VALUES (3,'Apple', 'Apple co.', 16.7, '2024-12-24 11:43:47.682033');
INSERT INTO STOCK (id, name, description, current_price, last_update) VALUES (4,'Hyundai', 'Hyundai co.', 10.4, '2024-12-24 11:43:47.682033');
INSERT INTO STOCK (id, name, description, current_price, last_update) VALUES (5,'Nvidia', 'Nvidia co.', 23.4, '2024-12-24 11:43:47.682033');
INSERT INTO STOCK (id, name, description, current_price, last_update) VALUES (6,'ING', 'ING bank', 12.7, '2024-12-24 11:43:47.682033');
INSERT INTO STOCK (id, name, description, current_price, last_update) VALUES (7,'Rabobank', 'Rabobank co.', 10.8, '2024-12-24 11:43:47.682033');
INSERT INTO STOCK (id, name, description, current_price, last_update) VALUES (8,'Tesla', 'Tesla co.', 29.5, '2024-12-24 11:43:47.682033');
ALTER TABLE STOCK ALTER COLUMN id RESTART WITH 9;

INSERT INTO STOCK_STOCK_EXCHANGE(stock_exchange_id, stock_id) VALUES (1, 1);
INSERT INTO STOCK_STOCK_EXCHANGE(stock_exchange_id, stock_id) VALUES (1, 3);
INSERT INTO STOCK_STOCK_EXCHANGE(stock_exchange_id, stock_id) VALUES (1, 4);
INSERT INTO STOCK_STOCK_EXCHANGE(stock_exchange_id, stock_id) VALUES (1, 5);
INSERT INTO STOCK_STOCK_EXCHANGE(stock_exchange_id, stock_id) VALUES (1, 8);
INSERT INTO STOCK_STOCK_EXCHANGE(stock_exchange_id, stock_id) VALUES (2, 1);
INSERT INTO STOCK_STOCK_EXCHANGE(stock_exchange_id, stock_id) VALUES (2, 2);
INSERT INTO STOCK_STOCK_EXCHANGE(stock_exchange_id, stock_id) VALUES (2, 6);
INSERT INTO STOCK_STOCK_EXCHANGE(stock_exchange_id, stock_id) VALUES (2, 5);
INSERT INTO STOCK_STOCK_EXCHANGE(stock_exchange_id, stock_id) VALUES (2, 8);
INSERT INTO STOCK_STOCK_EXCHANGE(stock_exchange_id, stock_id) VALUES (3, 2);
INSERT INTO STOCK_STOCK_EXCHANGE(stock_exchange_id, stock_id) VALUES (3, 3);
INSERT INTO STOCK_STOCK_EXCHANGE(stock_exchange_id, stock_id) VALUES (3, 7);
INSERT INTO STOCK_STOCK_EXCHANGE(stock_exchange_id, stock_id) VALUES (3, 6);
INSERT INTO STOCK_STOCK_EXCHANGE(stock_exchange_id, stock_id) VALUES (3, 1);

INSERT INTO USERS(id, email_id, name, password) VALUES (1, 'john.doe@test.com', 'John Doe', '$2a$10$QkIZasNQm6aJkOOWmivRwuyrYj4oc0fACZd8yCAYcfNVLF0Tn2v2q');
INSERT INTO USERS(id, email_id, name, password) VALUES (2, 'jane.doe@test.com', 'Jane Doe', '$2a$10$QkIZasNQm6aJkOOWmivRwuyrYj4oc0fACZd8yCAYcfNVLF0Tn2v2q');