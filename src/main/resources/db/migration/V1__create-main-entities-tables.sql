CREATE TABLE hotels(
    hotel_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL UNIQUE,
    category TINYINT NOT NULL,
    address VARCHAR(300) NOT NULL,
    phone CHAR(10) NOT NULL UNIQUE,
    director BIGINT NULL
);

CREATE TABLE employees(
    employee_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(10) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL UNIQUE,
    address VARCHAR(300) NOT NULL,
    type VARCHAR(100) NOT NULL,
    hotel BIGINT NULL
);

CREATE TABLE customers(
    customer_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(10) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL UNIQUE,
    address VARCHAR(300) NOT NULL,
    phone CHAR(10) NOT NULL UNIQUE
);

ALTER TABLE employees ADD CONSTRAINT fk_employee_hotel FOREIGN KEY(hotel) REFERENCES hotels(hotel_id);

ALTER TABLE hotels ADD CONSTRAINT fk_hotel_employee FOREIGN KEY(director) REFERENCES employees(employee_id);



