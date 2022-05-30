-- -----------------------------------------------------
-- Table `module_3`.`gift_certificates`
-- -----------------------------------------------------
INSERT INTO module_3.gift_certificates (name, description, price, duration, create_date, last_update_date)
VALUES ('Oz.by', 'Books, games, stationery', 20, 40, '2022-04-11 13:48:14.000', '2022-04-11 13:48:14.000'),
       ('Belvest', 'Change two shoes', 50, 30, '2022-04-12 13:48:14.000', '2022-04-12 13:48:14.000'),
       ('Evroopt', 'We have a lot of sugar!', 40, 10, '2022-04-13 13:48:14.000', '2022-04-13 13:48:14.000'),
       ('Evroopt', 'Buy two bananas', 20, 10, '2022-04-10 13:48:14.000', '2022-04-10 13:48:14.000');

-- -----------------------------------------------------
-- Table `module_3`.`tags`
-- -----------------------------------------------------
INSERT INTO module_3.tags (name)
VALUES ('food'), ('stationery'), ('shoe'), ('virtual'), ('paper'), ('by');

-- -----------------------------------------------------
-- Table `module_3`.`gift_certificate_tag`
-- -----------------------------------------------------
INSERT INTO module_3.gift_certificate_tag (gift_certificate_id, tag_id)
VALUES (1, 2), (1, 4), (1, 6), (2, 3), (2, 5), (2, 6), (3, 1),
       (3, 2), (3, 5), (3, 6), (4, 1), (4, 2), (4, 5), (4, 6);

-- -----------------------------------------------------
-- Table `module_3`.`users`
-- -----------------------------------------------------
INSERT INTO module_3.users (first_name, last_name)
VALUES ('Ivan', 'Pupkin'),
       ('Sanek', 'Lupkin');

-- -----------------------------------------------------
-- Table `module_3`.`orders`
-- -----------------------------------------------------
INSERT INTO module_3.orders (cost, created_date, user_id, gift_certificate_id)
VALUES (20, '2022-05-26 22:25:17.000', 1, 1),
       (10, '2022-05-27 22:25:17.000', 1, 2),
       (5, '2022-05-28 22:25:17.000', 1, 2),
       (20, '2022-05-26 22:25:17.000', 2, 1),
       (20, '2022-05-28 22:25:17.000', 2, 4);