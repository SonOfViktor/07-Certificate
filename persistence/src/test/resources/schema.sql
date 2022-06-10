CREATE SCHEMA module_two;
-- -----------------------------------------------------
-- Table `module_two`.`gift_certificate`
-- -----------------------------------------------------
 CREATE TABLE IF NOT EXISTS `module_two`.`gift_certificate` (
    `gift_certificate_id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(45) NOT NULL,
    `description` VARCHAR(500) NULL,
    `price` DECIMAL(6,2) NOT NULL DEFAULT 0,
    `duration` INT NOT NULL,
    `create_date` TIMESTAMP(3) NOT NULL ,
    `last_update_date` TIMESTAMP(3) NOT NULL,
    PRIMARY KEY (`gift_certificate_id`));
  --  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `module_two`.`tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `module_two`.`tag` (
    `tag_id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(45) NOT NULL,
    CONSTRAINT name_unique UNIQUE (`name`),
    PRIMARY KEY (`tag_id`));
--   ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `module_two`.`gift_certificate_tag`
-- -----------------------------------------------------
 CREATE TABLE IF NOT EXISTS `module_two`.`gift_certificate_tag` (
    `gct_gift_certificate_id` INT NOT NULL,
    `gct_tag_id` INT NOT NULL,
    PRIMARY KEY (`gct_gift_certificate_id`, `gct_tag_id`),
    CONSTRAINT `fk_gift_certificate`
        FOREIGN KEY (`gct_gift_certificate_id`)
        REFERENCES `gift_certificate` (`gift_certificate_id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT `fk_tag`
        FOREIGN KEY (`gct_tag_id`)
        REFERENCES `tag` (`tag_id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE);
 --   ENGINE = InnoDB;