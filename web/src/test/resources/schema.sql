CREATE SCHEMA IF NOT EXISTS module_7;

-- -----------------------------------------------------
-- Table `module_7`.`categories`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `module_7`.`categories`
(
    `id`      INT         NOT NULL AUTO_INCREMENT,
    `name`    VARCHAR(15) NOT NULL,
    `picture` VARCHAR(30) NOT NULL,
    UNIQUE (`name`),
    PRIMARY KEY (`id`)
);

-- -----------------------------------------------------
-- Table `module_7`.`gift_certificates`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `module_7`.`gift_certificates`
(
    `id`               INT           NOT NULL AUTO_INCREMENT,
    `name`             VARCHAR(30)   NOT NULL,
    `description`      VARCHAR(1000)  NULL,
    `price`            DECIMAL(6, 2) NOT NULL DEFAULT 0,
    `duration`         INT           NOT NULL,
    `image`            VARCHAR(50)   NOT NULL,
    `create_date`      TIMESTAMP(3)  NOT NULL,
    `last_update_date` TIMESTAMP(3)  NOT NULL,
    `category_id`      INT           NOT NULL REFERENCES `module_7`.`categories` (`id`),
    PRIMARY KEY (`id`)
);

-- -----------------------------------------------------
-- Table `module_7`.`tags`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `module_7`.`tags`
(
    `id`   INT         NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(15) NOT NULL,
    UNIQUE (`name`),
    PRIMARY KEY (`id`)
);

-- -----------------------------------------------------
-- Table `module_7`.`gift_certificate_tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `module_7`.`gift_certificate_tag`
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
-- Table `module_7`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `module_7`.`users`
(
    `id`         INT          NOT NULL AUTO_INCREMENT,
    `email`      VARCHAR(255) NOT NULL UNIQUE,
    `password`   VARCHAR(100) NOT NULL,
    `first_name` VARCHAR(50)  NOT NULL,
    `last_name`  VARCHAR(50)  NOT NULL,
    `role`       VARCHAR(20)  NOT NULL,
    PRIMARY KEY (`id`)
);

-- -----------------------------------------------------
-- Table `module_7`.`payments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `module_7`.`payments`
(
    `id`           INT          NOT NULL AUTO_INCREMENT,
    `user_id`      INT          NOT NULL REFERENCES `module_7`.`users` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    `created_date` TIMESTAMP(3) NOT NULL,
    PRIMARY KEY (`id`)
);

-- -----------------------------------------------------
-- Table `module_7`.`orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `module_7`.`orders`
(
    `id`                  INT           NOT NULL AUTO_INCREMENT,
    `cost`                NUMERIC(5, 2) NOT NULL CHECK (`cost` > 0),
    `gift_certificate_id` INT           REFERENCES `module_7`.`gift_certificates` (`id`)
                                            ON UPDATE CASCADE
                                            ON DELETE SET NULL,
    `payment_id`          INT           NOT NULL REFERENCES `module_7`.`payments` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    PRIMARY KEY (`id`)
);