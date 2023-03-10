DROP DATABASE IF EXISTS BOOKSTORE;
CREATE DATABASE BOOKSTORE;
USE BOOKSTORE;

DROP USER IF EXISTS Manager@localhost;

CREATE USER Manager@'localhost' IDENTIFIED BY 'cookie';

GRANT ALL ON BOOKSTORE.* TO Manager@'localhost';

FLUSH PRIVILEGES;