-- -----------------------------------------------------
-- Table `module_7`.`categories`
-- -----------------------------------------------------
INSERT INTO module_7.categories (name, picture)
VALUES ('Tech', 'category/tech.jpg'),
       ('Food', 'category/food.jpg');

-- -----------------------------------------------------
-- Table `module_7`.`gift_certificates`
-- -----------------------------------------------------
INSERT INTO module_7.gift_certificates (name, description, price, duration, image, create_date, last_update_date, category_id)
VALUES ('Oz.by', 'Books, games, stationery', 20, 40, 'test/001.jpg', '2022-04-11 13:48:14.000', '2022-04-11 13:48:14.000', 1),
       ('Belvest', 'Change two shoes', 50, 30, 'test/001.jpg', '2022-04-12 13:48:14.000', '2022-04-12 13:48:14.000', 2),
       ('Evroopt', 'We have a lot of sugar!', 40, 10, 'test/001.jpg', '2022-04-13 13:48:14.000', '2022-04-13 13:48:14.000', 1),
       ('Evroopt', 'Buy two bananas', 20, 10, 'test/001.jpg', '2022-04-10 13:48:14.000', '2022-04-10 13:48:14.000', 2);

-- -----------------------------------------------------
-- Table `module_7`.`tags`
-- -----------------------------------------------------
INSERT INTO module_7.tags (name)
VALUES ('food'),
       ('stationery'),
       ('shoe'),
       ('virtual'),
       ('paper'),
       ('game');

-- -----------------------------------------------------
-- Table `module_7`.`gift_certificate_tag`
-- -----------------------------------------------------
INSERT INTO module_7.gift_certificate_tag (gift_certificate_id, tag_id)
VALUES (1, 2),
       (1, 4),
       (2, 3),
       (2, 5),
       (2, 6),
       (3, 1),
       (3, 2),
       (3, 5),
       (3, 6),
       (4, 1),
       (4, 2),
       (4, 5),
       (4, 6);

-- -----------------------------------------------------
-- Table `module_7`.`users`
-- -----------------------------------------------------
INSERT INTO module_7.users (email, password, first_name, last_name, role)
VALUES ('ivan_pupkin@gmail.com',
        '$2a$12$8jtm/F2lKTrxJKR1N4z1m.RTFmtAn7VIQ2IHLjtk/wTxd.2Vlmkb6', 'Ivan', 'Pupkin', 'ADMIN'),
       ('sanek_lupkin.gmail.com',
        '$2a$12$8jtm/F2lKTrxJKR1N4z1m.RTFmtAn7VIQ2IHLjtk/wTxd.2Vlmkb6','Sanek', 'Lupkin', 'USER');

-- -----------------------------------------------------
-- Table `module_7`.`payments`
-- -----------------------------------------------------
INSERT INTO module_7.payments (user_id, created_date)
VALUES (1, '2022-05-26 22:25:17.000'),
       (1, '2022-05-27 22:25:17.000'),
       (1, '2022-05-28 22:25:17.000'),
       (2, '2022-05-26 22:25:17.000'),
       (2, '2022-05-28 22:25:17.000');

-- -----------------------------------------------------
-- Table `module_7`.`orders`
-- -----------------------------------------------------
INSERT INTO module_7.orders (cost, payment_id, gift_certificate_id)
VALUES (20, 1, 1),
       (10, 1, 2),
       (5, 2, 2),
       (20, 2, 3),
       (20, 3, 4),
       (20, 4, 4),
       (20, 4, 4),
       (20, 5, 2),
       (20, 5, 3);