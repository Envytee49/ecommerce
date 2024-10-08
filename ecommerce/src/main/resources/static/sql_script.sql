-- MySQL Script generated by MySQL Workbench
-- Thu May 30 13:59:39 2024
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema nest_ecom
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `nest_ecom` ;

-- -----------------------------------------------------
-- Schema nest_ecom
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `nest_ecom` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
SHOW WARNINGS;
USE `nest_ecom` ;


-- -----------------------------------------------------
-- Table `attribute`
-- -----------------------------------------------------
-- DROP TABLE IF EXISTS `attribute` ;

-- SHOW WARNINGS;
-- CREATE TABLE IF NOT EXISTS `attribute` (
--   `uuid_attribute` VARCHAR(40) NOT NULL,
--   `_key` VARCHAR(40) NULL DEFAULT NULL,
--   `created_date` TIMESTAMP NULL DEFAULT NULL,
--   `updated_date` TIMESTAMP NULL DEFAULT NULL,
--   PRIMARY KEY (`uuid_attribute`))
-- ENGINE = InnoDB
-- DEFAULT CHARACTER SET = utf8mb4
-- COLLATE = utf8mb4_0900_ai_ci;

-- SHOW WARNINGS;
-- -----------------------------------------------------
-- Table `user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `user` (
  `uuid_user` VARCHAR(40) NOT NULL,
  `uuid_cart` VARCHAR(40) NOT NULL,
  `username` VARCHAR(50) NULL DEFAULT NULL,
  `mobile` VARCHAR(15) NULL DEFAULT NULL,
  `email` VARCHAR(50) NULL DEFAULT NULL,
  `avatar` VARCHAR(200) NULL DEFAULT NULL,
  `description` TEXT NULL DEFAULT NULL,
  `password` VARCHAR(200) NOT NULL,
  `register_date` TIMESTAMP NOT NULL,
  `last_login` TIMESTAMP NULL DEFAULT NULL,
  `activate` SMALLINT NULL DEFAULT '0',
  PRIMARY KEY (`uuid_user`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

SHOW WARNINGS;
CREATE UNIQUE INDEX `uuid_cart` ON `user` (`uuid_cart` ASC) VISIBLE;

CREATE UNIQUE INDEX `user_email` ON `user` (`email` ASC) VISIBLE;
-- -----------------------------------------------------
-- Table `user_role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user_role` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `user_role` (
  `uuid_user` VARCHAR(40) NOT NULL,
  `role` enum("ADMIN", "SELLER", "USER") NOT NULL,
  PRIMARY KEY (`uuid_user`, `role`),
  CONSTRAINT `user_role_fk` FOREIGN KEY (`uuid_user`) REFERENCES `user` (`uuid_user`)
  )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;
-- -----------------------------------------------------
-- Table `brand`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shop` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `shop` (
  `uuid_shop` VARCHAR(40) NOT NULL,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `created_date` TIMESTAMP NULL DEFAULT NULL,
  `updated_date` TIMESTAMP NULL DEFAULT NULL,
  `uuid_seller` VARCHAR(40) NOT NULL,
  PRIMARY KEY (`uuid_shop`),
  CONSTRAINT `user_shop_uuid_user_fk`
    FOREIGN KEY (`uuid_seller`)
    REFERENCES `user` (`uuid_user`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `brand`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shop_address` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `shop_address` (
  `uuid_saddress` VARCHAR(40) NOT NULL,
  `seller_name` VARCHAR(255) NULL DEFAULT NULL,
  `mobile` VARCHAR(15) NULL DEFAULT NULL,
  `city` VARCHAR(255) NULL DEFAULT NULL,
  `street` VARCHAR(255) NULL DEFAULT NULL,
  `district` VARCHAR(255) NULL DEFAULT NULL,
  `postal_code` INT NULL DEFAULT NULL,
  `uuid_shop` VARCHAR(40) NOT NULL,
  PRIMARY KEY (`uuid_saddress`),
  CONSTRAINT `shop_uuid_user_fk`
    FOREIGN KEY (`uuid_shop`)
    REFERENCES `shop` (`uuid_shop`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SHOW WARNINGS;
-- -----------------------------------------------------
-- Table `product`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `product` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `product` (
  `uuid_product` VARCHAR(40) NOT NULL,
  `title` VARCHAR(75) NOT NULL,
  `meta_title` VARCHAR(100) NULL DEFAULT NULL,
  `summary` TEXT NULL DEFAULT NULL,
  `price` DOUBLE NOT NULL DEFAULT '0',
  `discount` decimal(3,2) default 0 CHECK (`discount` BETWEEN 0.00 AND 1.00),
  `quantity` SMALLINT NOT NULL DEFAULT '0',
  `created_date` TIMESTAMP NOT NULL,
  `updated_date` TIMESTAMP NULL DEFAULT NULL,
  `published_date` TIMESTAMP NULL DEFAULT NULL,
  `description` TEXT NULL DEFAULT NULL,
  `uuid_shop` VARCHAR(40) NOT NULL,
	PRIMARY KEY(`uuid_product`),
	CONSTRAINT `product_shop_uuid_seller_fk`
    FOREIGN KEY (`uuid_shop`)
    REFERENCES `shop` (`uuid_shop`)
    )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

CREATE UNIQUE INDEX `product_title` ON `product` (`title` ASC) VISIBLE;

drop table if exists product_variant;
CREATE TABLE IF NOT EXISTS `product_variant` (
  `uuid_product_variant` VARCHAR(36) NOT NULL,
  `uuid_product` VARCHAR(40) NOT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`uuid_product_variant`),
  UNIQUE KEY `UNIQUE_product_id_name` (`uuid_product`,`name`)
)ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

--
-- Dumping data for table `product_variant`
--

INSERT INTO `product_variant` (`uuid_product_variant`, `uuid_product`, `name`) VALUES
('1eb4f263-48b3-4900-bd83-beebe9808c63', '02e8ccf6-05b5-4db9-b26a-fc4e6c56f972', 'Size'),
('27a2c4ae-dbd6-4b7e-a3a2-6c03b08cd26b', '02e8ccf6-05b5-4db9-b26a-fc4e6c56f972', 'Color'),

('55ab5946-dc10-4bdb-a8d5-24324c3b6574', '2ef1523d-27bf-4318-93e1-df59bc6ada51', 'Size'),
('77f04d02-291d-4fe5-98c0-f2d96364730d', '2ef1523d-27bf-4318-93e1-df59bc6ada51', 'Color'),

('12039af5-3001-4481-a413-84974950b964', '784e3346-aebe-48d9-9327-a73554c3ce22', 'Size'),
('7c0a396c-0933-49c2-891b-db9168edad4c', '784e3346-aebe-48d9-9327-a73554c3ce22', 'Color'),

('b9936167-4e01-4e73-bba9-1ee600d2a75e', 'b22ddaf2-e7c8-410b-9ff4-e6aa1cac30b6', 'Size'),
('3e7c5487-c30d-46c3-9a20-47a60b55b998', 'b22ddaf2-e7c8-410b-9ff4-e6aa1cac30b6', 'Color');

-- --------------------------------------------------------

--
-- Table structure for table `product_variant_option`
--
drop table if exists product_variant_option;
CREATE TABLE IF NOT EXISTS `product_variant_option` (
  `uuid_product_variant_option` VARCHAR(36) NOT NULL ,
  `uuid_product_variant` VARCHAR(36) NOT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`uuid_product_variant_option`),
  UNIQUE KEY `UNIQUE_product_variant_id_name` (`uuid_product_variant`,`name`)
)ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

--
-- Dumping data for table `product_variant_option`
--

INSERT INTO `product_variant_option` (`uuid_product_variant_option`, `uuid_product_variant`, `name`) VALUES
('9db66a6e-5f6c-4c8b-b087-6968087ee80e', '1eb4f263-48b3-4900-bd83-beebe9808c63', 'Large'),
('3390b49d-2aca-4ec9-bf64-3dc138ad6d81','1eb4f263-48b3-4900-bd83-beebe9808c63', 'Small'),
('b5af5e48-2c15-42ab-962a-c1fba2aa14ce', '27a2c4ae-dbd6-4b7e-a3a2-6c03b08cd26b', 'Black'),
('aab52b75-debe-477d-9afc-5ff415382487', '27a2c4ae-dbd6-4b7e-a3a2-6c03b08cd26b', 'White'),

('0c0b613b-6014-4755-8712-d4df1bab0be2', '55ab5946-dc10-4bdb-a8d5-24324c3b6574', 'Large'),
('af8dd210-d41d-4fa2-90d7-3ef2cd26886c','55ab5946-dc10-4bdb-a8d5-24324c3b6574', 'Small'),
('754393b5-64d5-4748-8145-d59b688adbaa', '77f04d02-291d-4fe5-98c0-f2d96364730d', 'Red'),
('5c4ab121-5d23-465c-aa2a-4236a30e417b', '77f04d02-291d-4fe5-98c0-f2d96364730d', 'Blue'),

('dfc2fdc2-e893-4e5c-a224-32edff6f1400', '12039af5-3001-4481-a413-84974950b964', 'Large'),
('8ea0f521-fda3-470e-8153-9e58f513d2c6','12039af5-3001-4481-a413-84974950b964', 'Small'),
('e495ee64-411c-4d65-a692-415562d0c5b7', '7c0a396c-0933-49c2-891b-db9168edad4c', 'Grey'),
('a0dc3c75-3a7d-439d-98bc-84c3fdece4e6', '7c0a396c-0933-49c2-891b-db9168edad4c', 'Pink'),

('8ea08453-ca4a-4779-b782-fccc4db60105', 'b9936167-4e01-4e73-bba9-1ee600d2a75e', 'Large'),
('2073d13a-9785-4811-8fc6-d68aaf6033fa','b9936167-4e01-4e73-bba9-1ee600d2a75e', 'Small'),
('08a022d4-c816-44a3-b55b-68d262397ba1', '3e7c5487-c30d-46c3-9a20-47a60b55b998', 'Green'),
('a4072c9f-d0aa-41c7-a3a5-a6bf8b2cd3c9', '3e7c5487-c30d-46c3-9a20-47a60b55b998', 'Purple');

-- --------------------------------------------------------

--
-- Table structure for table `sku`
--
drop table if exists sku;

CREATE TABLE IF NOT EXISTS `sku` (
  `uuid_sku` VARCHAR(36) NOT NULL ,
  `uuid_product` VARCHAR(40) NOT NULL,
  `sku` varchar(45) NOT NULL,
  `discount` decimal(3,2) default 0 CHECK (`discount` BETWEEN 0.00 AND 1.00),
  `price` decimal(10,2) NOT NULL,
  `quantity` integer NOT NULL,
  PRIMARY KEY (`uuid_sku`),
  KEY `skus_product_id_products_id_idx` (`uuid_product`)
)ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

--
-- Dumping data for table `sku`
--

INSERT INTO `sku` (`uuid_sku`, `uuid_product`, `sku`, `price`, `quantity`) VALUES
('10dc4b19-3e11-41c8-b194-62b2d5e034d8', '02e8ccf6-05b5-4db9-b26a-fc4e6c56f972', 'W1', '120.99', 5),
('55374df5-91bd-465b-8246-936b00bc38a6', '02e8ccf6-05b5-4db9-b26a-fc4e6c56f972', 'W2', '125.99', 3),
('3da27443-ad38-4b27-afc7-237417da2c09', '02e8ccf6-05b5-4db9-b26a-fc4e6c56f972', 'W3', '130.99', 4),
('d0671159-7cc4-4d3c-944e-5b0faad1c64c', '02e8ccf6-05b5-4db9-b26a-fc4e6c56f972', 'W4', '135.99', 1),

('70b399a5-f7c0-499b-b3c9-4fcda7516551', '2ef1523d-27bf-4318-93e1-df59bc6ada51', 'W5', '150', 3),
('acaef534-4b92-4dba-bb05-2c3611146c79', '2ef1523d-27bf-4318-93e1-df59bc6ada51', 'W6', '155', 2),
('a6ee6f05-d1e8-4ce4-aa92-294d9771e1d5', '2ef1523d-27bf-4318-93e1-df59bc6ada51', 'W7', '160', 4),
('8b81fcff-de4a-46a1-93de-d0b17b4c39f0', '2ef1523d-27bf-4318-93e1-df59bc6ada51', 'W8', '165', 1),

('901a1f9f-976d-427a-bd75-c57acf35be71', '784e3346-aebe-48d9-9327-a73554c3ce22', 'W9', '140', 10),
('91905a8f-a455-47e9-b02a-5e04b0ed06de', '784e3346-aebe-48d9-9327-a73554c3ce22', 'W10', '145', 34),
('06af0756-209c-4601-9c68-28b2ae77c4c3', '784e3346-aebe-48d9-9327-a73554c3ce22', 'W11', '155', 3),
('57636f83-1ccf-4629-a5f1-4e260a32c9cc', '784e3346-aebe-48d9-9327-a73554c3ce22', 'W12', '130', 1),

('7a894865-0849-49e9-b627-3862854b03e8', 'b22ddaf2-e7c8-410b-9ff4-e6aa1cac30b6', 'W13', '110', 3),
('3d2dea90-4988-47b9-8988-57e78bc94964', 'b22ddaf2-e7c8-410b-9ff4-e6aa1cac30b6', 'W14', '115', 2),
('4fb0af92-ed90-40b4-a496-0b62c3a093f9', 'b22ddaf2-e7c8-410b-9ff4-e6aa1cac30b6', 'W15', '120', 12),
('e0410521-e4dc-4cb4-b0c5-dada8a58b21f', 'b22ddaf2-e7c8-410b-9ff4-e6aa1cac30b6', 'W16', '125', 1);
-- --------------------------------------------------------

--
-- Table structure for table `sku_product_variant_options`
--
DROP TABLE IF exists sku_product_variant_option;

CREATE TABLE IF NOT EXISTS `sku_product_variant_option` (
  `uuid_sku` VARCHAR(40) NOT NULL,
  `uuid_product_variant` VARCHAR(40) NOT NULL,
  `uuid_product_variant_option` VARCHAR(40) NOT NULL,
  PRIMARY KEY (`uuid_sku`,`uuid_product_variant_option`,`uuid_product_variant`),
  UNIQUE KEY `UNIQUE_sku_id_product_variant_id` (`uuid_sku`,`uuid_product_variant`),
  KEY `spvo_product_variant_options_id_pro_idx` (`uuid_product_variant_option`),
  KEY `spvo_product_variant_id_product_var_idx` (`uuid_product_variant`)
)ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

--
-- Dumping data for table `sku_product_variant_options`
--

INSERT INTO `sku_product_variant_option` (`uuid_sku`, `uuid_product_variant`, `uuid_product_variant_option`) VALUES
-- 1
('10dc4b19-3e11-41c8-b194-62b2d5e034d8', '1eb4f263-48b3-4900-bd83-beebe9808c63', '9db66a6e-5f6c-4c8b-b087-6968087ee80e'),
('10dc4b19-3e11-41c8-b194-62b2d5e034d8', '27a2c4ae-dbd6-4b7e-a3a2-6c03b08cd26b', 'b5af5e48-2c15-42ab-962a-c1fba2aa14ce'),

('55374df5-91bd-465b-8246-936b00bc38a6', '1eb4f263-48b3-4900-bd83-beebe9808c63', '9db66a6e-5f6c-4c8b-b087-6968087ee80e'),
('55374df5-91bd-465b-8246-936b00bc38a6', '27a2c4ae-dbd6-4b7e-a3a2-6c03b08cd26b', 'aab52b75-debe-477d-9afc-5ff415382487'),

('3da27443-ad38-4b27-afc7-237417da2c09', '1eb4f263-48b3-4900-bd83-beebe9808c63', '3390b49d-2aca-4ec9-bf64-3dc138ad6d81'),
('3da27443-ad38-4b27-afc7-237417da2c09', '27a2c4ae-dbd6-4b7e-a3a2-6c03b08cd26b', 'b5af5e48-2c15-42ab-962a-c1fba2aa14ce'),

('d0671159-7cc4-4d3c-944e-5b0faad1c64c', '1eb4f263-48b3-4900-bd83-beebe9808c63', '3390b49d-2aca-4ec9-bf64-3dc138ad6d81'),
('d0671159-7cc4-4d3c-944e-5b0faad1c64c', '27a2c4ae-dbd6-4b7e-a3a2-6c03b08cd26b', 'aab52b75-debe-477d-9afc-5ff415382487'),

-- 2

('70b399a5-f7c0-499b-b3c9-4fcda7516551', '55ab5946-dc10-4bdb-a8d5-24324c3b6574','0c0b613b-6014-4755-8712-d4df1bab0be2'),
('70b399a5-f7c0-499b-b3c9-4fcda7516551','77f04d02-291d-4fe5-98c0-f2d96364730d', '754393b5-64d5-4748-8145-d59b688adbaa'),

('acaef534-4b92-4dba-bb05-2c3611146c79', '55ab5946-dc10-4bdb-a8d5-24324c3b6574', '0c0b613b-6014-4755-8712-d4df1bab0be2'),
('acaef534-4b92-4dba-bb05-2c3611146c79', '77f04d02-291d-4fe5-98c0-f2d96364730d', '5c4ab121-5d23-465c-aa2a-4236a30e417b'),

('a6ee6f05-d1e8-4ce4-aa92-294d9771e1d5', '55ab5946-dc10-4bdb-a8d5-24324c3b6574', 'af8dd210-d41d-4fa2-90d7-3ef2cd26886c'),
('a6ee6f05-d1e8-4ce4-aa92-294d9771e1d5', '77f04d02-291d-4fe5-98c0-f2d96364730d', '754393b5-64d5-4748-8145-d59b688adbaa'),

('8b81fcff-de4a-46a1-93de-d0b17b4c39f0', '55ab5946-dc10-4bdb-a8d5-24324c3b6574', 'af8dd210-d41d-4fa2-90d7-3ef2cd26886c'),
('8b81fcff-de4a-46a1-93de-d0b17b4c39f0','77f04d02-291d-4fe5-98c0-f2d96364730d', '5c4ab121-5d23-465c-aa2a-4236a30e417b'),
-- 3

('901a1f9f-976d-427a-bd75-c57acf35be71', '12039af5-3001-4481-a413-84974950b964', 'dfc2fdc2-e893-4e5c-a224-32edff6f1400'),
('901a1f9f-976d-427a-bd75-c57acf35be71', '7c0a396c-0933-49c2-891b-db9168edad4c', 'e495ee64-411c-4d65-a692-415562d0c5b7'),

('91905a8f-a455-47e9-b02a-5e04b0ed06de', '12039af5-3001-4481-a413-84974950b964', 'dfc2fdc2-e893-4e5c-a224-32edff6f1400'),
('91905a8f-a455-47e9-b02a-5e04b0ed06de', '7c0a396c-0933-49c2-891b-db9168edad4c', 'a0dc3c75-3a7d-439d-98bc-84c3fdece4e6'),

('06af0756-209c-4601-9c68-28b2ae77c4c3', '12039af5-3001-4481-a413-84974950b964', '8ea0f521-fda3-470e-8153-9e58f513d2c6'),
('06af0756-209c-4601-9c68-28b2ae77c4c3', '7c0a396c-0933-49c2-891b-db9168edad4c', 'e495ee64-411c-4d65-a692-415562d0c5b7'),

('57636f83-1ccf-4629-a5f1-4e260a32c9cc', '12039af5-3001-4481-a413-84974950b964', '8ea0f521-fda3-470e-8153-9e58f513d2c6'),
('57636f83-1ccf-4629-a5f1-4e260a32c9cc', '7c0a396c-0933-49c2-891b-db9168edad4c', 'a0dc3c75-3a7d-439d-98bc-84c3fdece4e6'),

-- 4

('7a894865-0849-49e9-b627-3862854b03e8', 'b9936167-4e01-4e73-bba9-1ee600d2a75e', '8ea08453-ca4a-4779-b782-fccc4db60105'),
('7a894865-0849-49e9-b627-3862854b03e8', '3e7c5487-c30d-46c3-9a20-47a60b55b998', '08a022d4-c816-44a3-b55b-68d262397ba1'),

('3d2dea90-4988-47b9-8988-57e78bc94964', 'b9936167-4e01-4e73-bba9-1ee600d2a75e', '8ea08453-ca4a-4779-b782-fccc4db60105'),
('3d2dea90-4988-47b9-8988-57e78bc94964', '3e7c5487-c30d-46c3-9a20-47a60b55b998', 'a4072c9f-d0aa-41c7-a3a5-a6bf8b2cd3c9'),

('4fb0af92-ed90-40b4-a496-0b62c3a093f9', 'b9936167-4e01-4e73-bba9-1ee600d2a75e', '2073d13a-9785-4811-8fc6-d68aaf6033fa'),
('4fb0af92-ed90-40b4-a496-0b62c3a093f9', '3e7c5487-c30d-46c3-9a20-47a60b55b998', '08a022d4-c816-44a3-b55b-68d262397ba1'),

('e0410521-e4dc-4cb4-b0c5-dada8a58b21f', 'b9936167-4e01-4e73-bba9-1ee600d2a75e', '2073d13a-9785-4811-8fc6-d68aaf6033fa'),
('e0410521-e4dc-4cb4-b0c5-dada8a58b21f','3e7c5487-c30d-46c3-9a20-47a60b55b998', 'a4072c9f-d0aa-41c7-a3a5-a6bf8b2cd3c9');
--
-- Constraints for dumped tables
--

--
-- Constraints for table `product_variant`
--
ALTER TABLE `product_variant`
  ADD CONSTRAINT `product_variants_product_id_products_id` 
  FOREIGN KEY (`uuid_product`) 
  REFERENCES `product` (`uuid_product`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `product_variant_option`
--
ALTER TABLE `product_variant_option`
  ADD CONSTRAINT `product_variant_options_product_variant_id_product_variants_id` 
  FOREIGN KEY (`uuid_product_variant`) 
  REFERENCES `product_variant` (`uuid_product_variant`) ON DELETE NO ACTION ON UPDATE NO ACTION;
--
-- Constraints for table `skus`
--
ALTER TABLE `sku`
  ADD CONSTRAINT `skus_product_id_products_id` 
  FOREIGN KEY (`uuid_product`) 
  REFERENCES `product` (`uuid_product`) ON DELETE NO ACTION ON UPDATE NO ACTION;
--
-- Constraints for table `sku_product_variant_options`
--
ALTER TABLE `sku_product_variant_option`
  ADD CONSTRAINT `sku_product_variant_options_sku_id_skus_id` 
  FOREIGN KEY (`uuid_sku`) 
  REFERENCES `sku` (`uuid_sku`)
  ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `spvo_product_variant_options_id_product_variant_options_id` 
  FOREIGN KEY (`uuid_product_variant_option`) 
  REFERENCES `product_variant_option` (`uuid_product_variant_option`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `spvo_product_variant_id_product_variants_id` 
  FOREIGN KEY (`uuid_product_variant`) 
  REFERENCES `product_variant` (`uuid_product_variant`) ON DELETE NO ACTION ON UPDATE NO ACTION;
 
-- -----------------------------------------------------
-- Table `cart_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cart_item` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `cart_item` (
	`uuid_cart_item` VARCHAR(40) NOT NULL,
  `uuid_cart` VARCHAR(40) NOT NULL,
  `uuid_product` VARCHAR(40) NOT NULL,
  `uuid_sku` VARCHAR(36) NULL,
  `price` DECIMAL(10,2) NOT NULL DEFAULT '0',
  `discount` DECIMAL(3,2) default 0 CHECK (`discount` BETWEEN 0.00 AND 1.00),
  `quantity` SMALLINT NOT NULL DEFAULT '0',
  `active` SMALLINT NOT NULL DEFAULT '0',
  `created_date` TIMESTAMP NOT NULL,
  `updated_date` TIMESTAMP NULL DEFAULT NULL,
  PRIMARY KEY (`uuid_cart_item`),
  CONSTRAINT `cart_item_fk_user_cart`
    FOREIGN KEY (`uuid_cart`)
    REFERENCES `user` (`uuid_cart`),
  CONSTRAINT `cart_item_fk_product`
    FOREIGN KEY (`uuid_product`)
    REFERENCES `product` (`uuid_product`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

SHOW WARNINGS;
CREATE INDEX `uuid_product` ON `cart_item` (`uuid_product` ASC) VISIBLE;

SHOW WARNINGS;
CREATE INDEX `uuid_cart_UNIQUE` ON `cart_item` (`uuid_cart` ASC) VISIBLE;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `category`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `category` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `category` (
  `uuid_category` VARCHAR(40) NOT NULL,
  `title` VARCHAR(75) NOT NULL,
  `meta_title` VARCHAR(100) NULL DEFAULT NULL,
  `slug` VARCHAR(100) NOT NULL,
  `content` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`uuid_category`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `order`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `order` ;
SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `order` (
  `uuid_order` VARCHAR(40) NOT NULL,
  `uuid_user` VARCHAR(40) NULL DEFAULT NULL,
  `uuid_shop` VARCHAR(40) NULL DEFAULT NULL,
  `status` enum('ORDER_PLACED', 'ORDER_APPROVED', 'ORDER_DECLINED', 'ORDER_SHIPPED', 'DELIVERED', 'CANCELLED') NOT NULL, -- enum or varchar here
  `merchandise_subtotal` DOUBLE NOT NULL DEFAULT '0',
  `shipping_subtotal` DOUBLE NOT NULL DEFAULT '0',
  `shipping_discount_subtotal` DOUBLE NOT NULL DEFAULT '0',
  `voucher_discount` DOUBLE NOT NULL DEFAULT '0',
  `total_payment` DOUBLE NOT NULL DEFAULT '0',
  `created_date` TIMESTAMP NOT NULL,
  `updated_date` TIMESTAMP NULL DEFAULT NULL,
  `payment_method` VARCHAR(10) NOT NULL,
  `note` TEXT NULL DEFAULT NULL,
  `uuid_uaddress` VARCHAR(40) NOT NULL,
  PRIMARY KEY (`uuid_order`),
  CONSTRAINT `order_ibfk_1`
    FOREIGN KEY (`uuid_user`)
    REFERENCES `user` (`uuid_user`),
	CONSTRAINT `order_ibfk_2`
    FOREIGN KEY (`uuid_uaddress`)
    REFERENCES `user_address` (`uuid_uaddress`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

SHOW WARNINGS;
CREATE INDEX `uuid_user` ON `order` (`uuid_user` ASC) VISIBLE;
SHOW WARNINGS;
CREATE INDEX `uuid_shop` ON `order` (`uuid_shop` ASC) VISIBLE;
-- -----------------------------------------------------
-- Table `cancel_order_reason`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cancel_order_reason` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `cancel_order_reason` (
	`uuid_reason` VARCHAR(40) NOT NULL PRIMARY KEY,
    `reason` VARCHAR(100) NOT NULL
)ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;
-- -----------------------------------------------------
-- Table `cancelled_order`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cancelled_order` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `cancelled_order` (
	`uuid_order` VARCHAR(40) NOT NULL PRIMARY KEY, 
    `uuid_reason`  VARCHAR(40) NOT NULL,
    `status` enum('APPROVED', 'DECLINED', 'PENDING') NOT NULL,
    `requested_at` TIMESTAMP NOT NULL,
    CONSTRAINT `cancelled_order_1`
    FOREIGN KEY (`uuid_order`)
    REFERENCES `order` (`uuid_order`),
    CONSTRAINT `cancelled_order_2`
    FOREIGN KEY (`uuid_reason`)
    REFERENCES `cancel_order_reason` (`uuid_reason`)
)ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `shop_voucher`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shop_voucher` ;
SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `shop_voucher` (
	`uuid_voucher` VARCHAR(40) NOT NULL,    
    `voucher_type` ENUM('ALL_SHOP', 'PRODUCTS') NOT NULL,
    `uuid_shop` VARCHAR(40) NULL DEFAULT NULL,
    `uuid_voucher_info` VARCHAR(40) NOT NULL,
    `uuid_voucher_constraint` VARCHAR(40) NOT NULL,
    PRIMARY KEY(`uuid_voucher`),
    CONSTRAINT `shop_voucher_ibfk_1`
    FOREIGN KEY (`uuid_shop`)
    REFERENCES `shop` (`uuid_shop`),
    CONSTRAINT `shop_voucher_ibfk_2`
    FOREIGN KEY (`uuid_voucher_info`)
    REFERENCES `voucher_info` (`uuid_voucher_info`),
    CONSTRAINT `shop_voucher_ibfk_3`
    FOREIGN KEY (`uuid_voucher_constraint`)
    REFERENCES `voucher_constraint` (`uuid_voucher_constraint`)
)ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `platform_voucher`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `platform_voucher` ;
SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `platform_voucher` (
	`uuid_voucher` VARCHAR(40) NOT NULL,    
    `voucher_type` ENUM('FREE_SHIPPING', 'DISCOUNT_CASHBACK') NOT NULL,
    `uuid_category` VARCHAR(40) NULL DEFAULT NULL,
    `uuid_voucher_info` VARCHAR(40) NOT NULL,
     `uuid_voucher_constraint` VARCHAR(40) NOT NULL,
    PRIMARY KEY(`uuid_voucher`),
    CONSTRAINT `platform_voucher_ibfk_1`
    FOREIGN KEY (`uuid_category`)
    REFERENCES `category` (`uuid_category`),
    CONSTRAINT `platform_voucher_ibfk_2`
    FOREIGN KEY (`uuid_voucher_info`)
    REFERENCES `voucher_info` (`uuid_voucher_info`),
    CONSTRAINT `platform_voucher_ibfk_3`
    FOREIGN KEY (`uuid_voucher_constraint`)
    REFERENCES `voucher_constraint` (`uuid_voucher_constraint`)
)ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `voucher_info`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `voucher_info` ;
SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `voucher_info` (
	`uuid_voucher_info` VARCHAR(40) NOT NULL,    
    `voucher_code` VARCHAR(20) NOT NULL,
    `quantity` INTEGER NOT NULL,
    `discount_type` ENUM('FIXED','PERCENTAGE'),
    `discount_percentage` DECIMAL(3,2) DEFAULT 0 CHECK(`discount_percentage` between 0.00 AND 1.00),
    `discount_value` DECIMAL(10,2) NULL DEFAULT NULL,
    `discount_cap` DOUBLE NOT NULL,
    `description` VARCHAR(200) NOT NULL,
    `is_visible` BOOLEAN DEFAULT TRUE,
    `created_date` TIMESTAMP NOT NULL,
	`updated_date` TIMESTAMP NULL DEFAULT NULL,
    PRIMARY KEY(`uuid_voucher_info`)
)ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;
-- -----------------------------------------------------
-- Table `percentage_discount`
-- -----------------------------------------------------
-- DROP TABLE IF EXISTS `percentage_discount` ;
-- SHOW WARNINGS;
-- CREATE TABLE IF NOT EXISTS `percentage_discount` (
--     `uuid_percentage_discount` VARCHAR(40) NOT NULL,
--     `uuid_voucher_info` VARCHAR(40) NOT NULL, 
--     `discount_percentage` DECIMAL(3,2) DEFAULT 0 CHECK(`discount_percentage` between 0.00 AND 1.00),
--     PRIMARY KEY(`uuid_percentage_discount`),
--     CONSTRAINT `pd_idx_1` 
--     FOREIGN KEY (`uuid_voucher_info`) 
--     REFERENCES `voucher_info` (`uuid_voucher_info`)
-- )ENGINE = InnoDB
-- DEFAULT CHARACTER SET = utf8mb4
-- COLLATE = utf8mb4_0900_ai_ci;
-- -- -----------------------------------------------------
-- -- Table `fixed_discount`
-- -- -----------------------------------------------------
-- DROP TABLE IF EXISTS `fixed_discount` ;
-- SHOW WARNINGS;
-- CREATE TABLE IF NOT EXISTS `fixed_discount` (
--     `uuid_fixed_discount` VARCHAR(40) NOT NULL,
--     `uuid_voucher_info` VARCHAR(40) NOT NULL, 
--     `discount_value` DECIMAL(10,2) DEFAULT 0,
--     PRIMARY KEY(`uuid_fixed_discount`),
--     CONSTRAINT `fd_idx_1` 
--     FOREIGN KEY (`uuid_voucher_info`) 
--     REFERENCES `voucher_info` (`uuid_voucher_info`)
-- )ENGINE = InnoDB
-- DEFAULT CHARACTER SET = utf8mb4
-- COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `voucher_constraint`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `voucher_constraint` ;
SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `voucher_constraint` (
	`uuid_voucher_constraint` VARCHAR(40) NOT NULL,
    `valid_from` DATE NOT NULL,
    `valid_until` DATE NOT NULL,
    `minimum_spend` DOUBLE NOT NULL,
    `max_usage` INTEGER NOT NULL,
    PRIMARY KEY(`uuid_voucher_constraint`)
)ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `voucher_constraint`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `voucher_constraint` ;
SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `voucher_constraint` (
	`uuid_voucher_constraint` VARCHAR(40) NOT NULL,
    `valid_from` DATE NOT NULL,
    `valid_until` DATE NOT NULL,
    `minimum_spend` DOUBLE NOT NULL,
    `max_usage` INTEGER NOT NULL,
    PRIMARY KEY(`uuid_voucher_constraint`)
)ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;
-- -----------------------------------------------------
-- Table `product_voucher`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `product_voucher` ;
SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `product_voucher` (
	`uuid_voucher` VARCHAR(40) NOT NULL,
    `uuid_product` VARCHAR(40) NOT NULL,
    PRIMARY KEY (`uuid_voucher`, `uuid_product`),
    CONSTRAINT `vp_ibfk_1`
    FOREIGN KEY (`uuid_voucher`)
    REFERENCES `abstractVoucher` (`uuid_voucher`),
    CONSTRAINT `vp_ibfk_2`
    FOREIGN KEY (`uuid_product`)
    REFERENCES `product` (`uuid_product`)
)ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;
-- -----------------------------------------------------
-- Table `voucher_redemption`
-- ----------------------------------------------------

DROP TABLE IF EXISTS `voucher_redemption` ;
SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `voucher_redemption` (
	`uuid_voucher` VARCHAR(40) NOT NULL,
    `uuid_user` VARCHAR(40) NOT NULL,
    `usage` INTEGER NOT NULL DEFAULT 0,
    `redemption_date` DATE NOT NULL,
    PRIMARY KEY (`uuid_voucher`, `uuid_user`),
    CONSTRAINT `vr_ibfk_2`
    FOREIGN KEY (`uuid_user`)
    REFERENCES `user` (`uuid_user`)
)ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;
CREATE INDEX `vr_idx_1` ON `voucher_redemption` (`uuid_voucher` ASC) VISIBLE;
-- -----------------------------------------------------
-- Table `order_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `order_item` ;
SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `order_item` (
  `uuid_order_item` VARCHAR(40) NOT NULL,
  `uuid_product` VARCHAR(40) NOT NULL,
   `uuid_sku` VARCHAR(36) NULL,
  `uuid_order` VARCHAR(40) NOT NULL,
  `price` DOUBLE NOT NULL DEFAULT '0',
  `discount` decimal(3,2) default 0 CHECK (`discount` BETWEEN 0.00 AND 1.00),
  `quantity` SMALLINT NOT NULL DEFAULT '0',
  `created_date` TIMESTAMP NOT NULL,
  `updated_date` TIMESTAMP NULL DEFAULT NULL,
  PRIMARY KEY (`uuid_order_item`),
  CONSTRAINT `order_item_ibfk_1`
    FOREIGN KEY (`uuid_product`)
    REFERENCES `product` (`uuid_product`),
  CONSTRAINT `order_item_ibfk_2`
    FOREIGN KEY (`uuid_order`)
    REFERENCES `order` (`uuid_order`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;
SHOW WARNINGS;
CREATE INDEX `uuid_product` ON `order_item` (`uuid_product` ASC) VISIBLE;

SHOW WARNINGS;
CREATE INDEX `uuid_order` ON `order_item` (`uuid_order` ASC) VISIBLE;
SHOW WARNINGS;


SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `product_attribute`
-- -----------------------------------------------------
-- DROP TABLE IF EXISTS `product_attribute` ;

-- SHOW WARNINGS;
-- CREATE TABLE IF NOT EXISTS `product_attribute` (
-- 	`uuid_product_attribute` VARCHAR(40) NOT NULL,
--   `uuid_attribute` VARCHAR(40) NOT NULL,
--   `uuid_product` VARCHAR(40) NOT NULL,
--   `_value` VARCHAR(200) NULL DEFAULT NULL,
--   PRIMARY KEY (`uuid_product_attribute`),
--   CONSTRAINT `fk_product_attribute_product1`
--     FOREIGN KEY (`uuid_product`)
--     REFERENCES `product` (`uuid_product`)
--     ON DELETE NO ACTION
--     ON UPDATE NO ACTION,
--   CONSTRAINT `fk_product_attribute_attribute1`
--     FOREIGN KEY (`uuid_attribute`)
--     REFERENCES `attribute` (`uuid_attribute`)
--     ON DELETE NO ACTION
--     ON UPDATE NO ACTION)
-- ENGINE = InnoDB
-- DEFAULT CHARACTER SET = utf8mb4
-- COLLATE = utf8mb4_0900_ai_ci;

-- SHOW WARNINGS;
-- CREATE INDEX `uuid_attribute` ON `product_attribute` (`uuid_attribute` ASC) VISIBLE;

-- SHOW WARNINGS;
-- CREATE INDEX `uuid_product` ON `product_attribute` (`uuid_product` ASC) VISIBLE;

-- SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `product_category`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `product_category` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `product_category` (
  `uuid_product` VARCHAR(40) NOT NULL,
  `uuid_category` VARCHAR(40) NOT NULL,
  CONSTRAINT `fk_pc_category`
    FOREIGN KEY (`uuid_category`)
    REFERENCES `category` (`uuid_category`),
  CONSTRAINT `fk_pc_product`
    FOREIGN KEY (`uuid_product`)
    REFERENCES `product` (`uuid_product`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

SHOW WARNINGS;
CREATE UNIQUE INDEX `uuid_product` ON `product_category` (`uuid_product` ASC) VISIBLE;

SHOW WARNINGS;
CREATE UNIQUE INDEX `uuid_category` ON `product_category` (`uuid_category` ASC) VISIBLE;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `product_review`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `product_review` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `product_review` (
  `uuid_product_review` VARCHAR(40) NOT NULL,
  `uuid_product` VARCHAR(40) NOT NULL,
  `uuid_user` VARCHAR(40) NOT NULL,
  `uuid_parent_product_review` VARCHAR(40) NULL DEFAULT NULL,
  `varitation` text NULL DEFAULT NULL,
  `comment` TEXT NULL DEFAULT NULL,
  `title` VARCHAR(100) NOT NULL,
  `rating` SMALLINT NOT NULL DEFAULT '0',
  `created_date` TIMESTAMP NOT NULL,
  `updated_date` TIMESTAMP NULL DEFAULT NULL,
  PRIMARY KEY (`uuid_product_review`),
  CONSTRAINT `product_review_ibfk_1`
    FOREIGN KEY (`uuid_product`)
    REFERENCES `product` (`uuid_product`),
  CONSTRAINT `product_review_ibfk_2`
    FOREIGN KEY (`uuid_parent_product_review`)
    REFERENCES `product_review` (`uuid_product_review`),
  CONSTRAINT `fk_product_review_user1`
    FOREIGN KEY (`uuid_user`)
    REFERENCES `user` (`uuid_user`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

SHOW WARNINGS;
CREATE INDEX `uuid_parent_product_review` ON `product_review` (`uuid_parent_product_review` ASC) VISIBLE;

SHOW WARNINGS;
CREATE INDEX `fk_product_review_user1_idx` ON `product_review` (`uuid_user` ASC) VISIBLE;

SHOW WARNINGS;
-- -----------------------------------------------------
-- Table `user_address`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user_address` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `user_address` (
  `uuid_uaddress` VARCHAR(40) NOT NULL,
  `uuid_user` VARCHAR(40) NULL DEFAULT NULL,
  `receiver_name` VARCHAR(30) NULL DEFAULT NULL,
  `mobile` VARCHAR(15) NULL DEFAULT NULL,
  `city` VARCHAR(255) NULL DEFAULT NULL,
  `street` VARCHAR(255) NULL DEFAULT NULL,
  `district` VARCHAR(255) NULL DEFAULT NULL,
  `postal_code` INT NULL DEFAULT NULL,
  PRIMARY KEY (`uuid_uaddress`),
  CONSTRAINT `user_address_ibfk_1`
    FOREIGN KEY (`uuid_user`)
    REFERENCES `user` (`uuid_user`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;
SHOW WARNINGS;
CREATE INDEX `uuid_user` ON `user_address` (`uuid_user` ASC) VISIBLE;
SHOW WARNINGS;
-- -----------------------------------------------------
-- Table `default_user_address`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `default_user_address` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `default_user_address` (
  `uuid_uaddress` VARCHAR(40) NOT NULL,
  `uuid_user` VARCHAR(40) NOT NULL,
  PRIMARY KEY (`uuid_user`),
  CONSTRAINT `default_user_address_ibfk_1`
    FOREIGN KEY (`uuid_user`)
    REFERENCES `user` (`uuid_user`),
  CONSTRAINT `default_user_address_ibfk_2`
    FOREIGN KEY (`uuid_uaddress`)
    REFERENCES `user_address` (`uuid_uaddress`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;
SHOW WARNINGS;
CREATE UNIQUE INDEX `uuid_uaddress` ON `default_user_address` (`uuid_uaddress` ASC) VISIBLE;
SHOW WARNINGS;
-- -----------------------------------------------------
-- Table `role`
-- -----------------------------------------------------
-- DROP TABLE IF EXISTS `role` ;

-- SHOW WARNINGS;
-- CREATE TABLE IF NOT EXISTS `role` (
--   `uuid_role` VARCHAR(40) NOT NULL,
--   `name` VARCHAR(20) NOT NULL,
--   PRIMARY KEY (`uuid_role`))
-- ENGINE = InnoDB
-- DEFAULT CHARACTER SET = utf8mb4
-- COLLATE = utf8mb4_0900_ai_ci;

-- SHOW WARNINGS;



-- SHOW WARNINGS;



-- -----------------------------------------------------
-- Table `user_role`
-- -----------------------------------------------------
-- DROP TABLE IF EXISTS `user_role` ;

-- SHOW WARNINGS;
-- CREATE TABLE IF NOT EXISTS `user_role` (
--   `uuid_user` VARCHAR(40) NOT NULL,
--   `uuid_role` VARCHAR(40) NOT NULL,
--   PRIMARY KEY (`uuid_role`, `uuid_user`),
--   CONSTRAINT `fk_user_role_user1`
--     FOREIGN KEY (`uuid_user`)
--     REFERENCES `user` (`uuid_user`)
--     ON DELETE NO ACTION
--     ON UPDATE NO ACTION,
--   CONSTRAINT `fk_user_role_role1`
--     FOREIGN KEY (`uuid_role`)
--     REFERENCES `role` (`uuid_role`)
--     ON DELETE NO ACTION
--     ON UPDATE NO ACTION)
-- ENGINE = InnoDB
-- DEFAULT CHARACTER SET = utf8mb4
-- COLLATE = utf8mb4_0900_ai_ci;

-- SHOW WARNINGS;
-- CREATE INDEX `user_role_pk` ON `user_role` (`uuid_user` ASC) VISIBLE;

-- SHOW WARNINGS;
-- CREATE INDEX `user_role_pk_2` ON `user_role` (`uuid_role` ASC) VISIBLE;

-- SHOW WARNINGS;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
