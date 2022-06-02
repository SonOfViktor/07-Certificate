-- -----------------------------------------------------
-- Table `module_two`.`gift_certificate`
-- -----------------------------------------------------
INSERT INTO `module_two`.`gift_certificate` (name, description, price, duration, create_date, last_update_date)
VALUES ('Oz.by', 'Books, games, stationery', 20, 40, '2022-04-11 13:48:14.000', '2022-04-11 13:48:14.000'),
       ('Belvest', 'Change two shoes', 50, 30, '2022-04-12 13:48:14.000', '2022-04-12 13:48:14.000'),
       ('Evroopt', 'We have a lot of sugar!', 40, 10, '2022-04-13 13:48:14.000', '2022-04-13 13:48:14.000'),
       ('Evroopt', 'Buy two bananas', 20, 10, '2022-04-10 13:48:14.000', '2022-04-10 13:48:14.000');

-- -----------------------------------------------------
-- Table `module_two`.`tag`
-- -----------------------------------------------------
INSERT INTO `module_two`.`tag` (name)
values ('food'), ('stationery'), ('shoe'), ('virtual'), ('paper'), ('by');

-- -----------------------------------------------------
-- Table `module_two`.`gift_certificate_tag`
-- -----------------------------------------------------
INSERT INTO`module_two`.`gift_certificate_tag` (gct_gift_certificate_id, gct_tag_id)
VALUES (1, 2), (1, 4), (1, 6), (2, 3), (2, 5), (2, 6), (3, 1),
       (3, 2), (3, 5), (3, 6), (4, 1), (4, 2), (4, 5), (4, 6);