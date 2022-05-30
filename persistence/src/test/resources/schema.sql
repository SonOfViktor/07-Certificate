CREATE SCHEMA module_3;
-- -----------------------------------------------------
-- Table `module_3`.`gift_certificates`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `module_3`.`gift_certificates`
(
    `id`               INT           NOT NULL AUTO_INCREMENT,
    `name`             VARCHAR(45)   NOT NULL,
    `description`      VARCHAR(500)  NULL,
    `price`            DECIMAL(6, 2) NOT NULL DEFAULT 0,
    `duration`         INT           NOT NULL,
    `create_date`      TIMESTAMP(3)  NOT NULL,
    `last_update_date` TIMESTAMP(3)  NOT NULL,
    PRIMARY KEY (`id`)
);

-- -----------------------------------------------------
-- Table `module_3`.`tags`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `module_3`.`tags`
(
    `id`   INT         NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(45) NOT NULL,
    UNIQUE (`name`),
    PRIMARY KEY (`id`)
);

-- -----------------------------------------------------
-- Table `module_3`.`gift_certificate_tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `module_3`.`gift_certificate_tag`
(
    `gift_certificate_id` INT NOT NULL,
    `tag_id`              INT NOT NULL,
    PRIMARY KEY (`gift_certificate_id`, `tag_id`),
    FOREIGN KEY (`gift_certificate_id`)
        REFERENCES `gift_certificates` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (`tag_id`)
        REFERENCES `tags` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- -----------------------------------------------------
-- Table `module_3`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `module_3`.`users`
(
    `id`         SERIAL PRIMARY KEY,
    `first_name` VARCHAR(50) NOT NULL,
    `last_name`  VARCHAR(50) NOT NULL
);

-- -----------------------------------------------------
-- Table `module_3`.`orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `module_3`.`orders`
(
    `id`                  SERIAL PRIMARY KEY,
    `cost`                NUMERIC(5, 2) NOT NULL CHECK ( `cost` > 0 ),
    `created_date`        TIMESTAMP(3)  NOT NULL,
    `user_id`             INT           NOT NULL REFERENCES module_3.users (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    `gift_certificate_id` INT REFERENCES `module_3`.`gift_certificates` (`id`)
        ON DELETE RESTRICT
);