CREATE SCHEMA IF NOT EXISTS module_7;

CREATE TABLE IF NOT EXISTS categories
(
    id SERIAL PRIMARY KEY ,
    name VARCHAR(15) UNIQUE NOT NULL,
    picture VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS gift_certificates
(
    id               SERIAL PRIMARY KEY,
    name             VARCHAR(45)   NOT NULL CHECK ( length(name) > 2 ),
    description      VARCHAR(1000)  NOT NULL,
    price            NUMERIC(5, 2) NOT NULL CHECK ( price > 0 ),
    duration         INT           NOT NULL CHECK ( duration > 0 AND duration < 100 ),
    image           VARCHAR(50) NOT NULL,
    create_date      TIMESTAMP(3)  NOT NULL,
    last_update_date TIMESTAMP(3)  NOT NULL,
    category_id     INT          NOT NULL REFERENCES categories (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS tags
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(45) NOT NULL UNIQUE CHECK ( length(name) > 2 )
);

CREATE TABLE IF NOT EXISTS gift_certificate_tag
(
    gift_certificate_id INT NOT NULL REFERENCES gift_certificates (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    tag_id              INT NOT NULL REFERENCES tags (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    UNIQUE (gift_certificate_id, tag_id)
);

CREATE TABLE IF NOT EXISTS users
(
    id         SERIAL PRIMARY KEY,
    email      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(100) NOT NULL,
    first_name VARCHAR(50)  NOT NULL,
    last_name  VARCHAR(50)  NOT NULL,
    role       VARCHAR(50)  NOT NULL
);

CREATE TABLE IF NOT EXISTS payments
(
    id           SERIAL PRIMARY KEY,
    user_id      INT          NOT NULL REFERENCES users (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    created_date TIMESTAMP(3) NOT NULL
);

CREATE TABLE IF NOT EXISTS orders
(
    id                  SERIAL PRIMARY KEY,
    cost                NUMERIC(5, 2) NOT NULL CHECK ( cost > 0 ),
    gift_certificate_id INT REFERENCES gift_certificates (id)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    payment_id          INT           NOT NULL REFERENCES payments (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

DROP TABLE IF EXISTS orders;

DROP TABLE IF EXISTS gift_certificate_tag;

DROP TABLE IF EXISTS gift_certificates;

DROP TABLE IF EXISTS payments CASCADE;

DROP TABLE IF EXISTS users CASCADE;

DROP TABLE IF EXISTS tags;

ALTER SEQUENCE tags_id_seq RESTART;
ALTER SEQUENCE gift_certificates_id_seq RESTART;
ALTER SEQUENCE orders_id_seq RESTART;
ALTER SEQUENCE payments_id_seq RESTART;
ALTER SEQUENCE users_id_seq RESTART;