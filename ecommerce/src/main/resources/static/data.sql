use nest_ecom;

INSERT INTO `user` (`uuid_user`, `uuid_cart`, `first_name`, `middle_name`, `last_name`, `mobile`, `email`, `avatar`, `description`, `password`, `register_date`, `last_login`, `activate`) 
VALUES 
('123e4567-e89b-12d3-a456-4266141740001234', '223e4567-e89b-12d3-a456-4266141740001234', 'John', 'A', 'Doe', '1234567890', 'john.doe@example.com', 'avatar1.jpg', 'A regular user', 'password123', '2024-06-20 12:00:00', '2024-06-21 12:00:00', 1),
('223e4567-e89b-12d3-a456-4266141740001234', '323e4567-e89b-12d3-a456-4266141740001235', 'Jane', 'B', 'Smith', '0987654321', 'jane.smith@example.com', 'avatar2.jpg', 'Another user', 'password456', '2024-06-20 13:00:00', '2024-06-21 13:00:00', 0);

INSERT INTO `brand` (`uuid_brand`, `name`, `created_date`, `updated_date`, `uuid_user`)
VALUES 
('323e4567-e89b-12d3-a456-4266141740001234', 'Nike', '2024-06-15 12:00:00', '2024-06-18 12:00:00', '123e4567-e89b-12d3-a456-4266141740001234'),
('423e4567-e89b-12d3-a456-4266141740001234', 'Adidas', '2024-06-16 12:00:00', '2024-06-19 12:00:00', '123e4567-e89b-12d3-a456-4266141740001234');

INSERT INTO `product` (`uuid_product`, `title`, `meta_title`, `summary`, `type`, `price`, `quantity`, `created_date`, `updated_date`, `published_date`, `description`, `uuid_brand`)
VALUES 
('523e4567-e89b-12d3-a456-4266141740001234', 'nike 1', 'nike shoes', 'very nice nike shoes', 0, 120.99, 50, '2024-06-10 10:00:00', '2024-06-12 10:00:00', '2024-06-13 10:00:00', 'This is nike shoes', '323e4567-e89b-12d3-a456-4266141740001234'),
('623e4567-e89b-12d3-a456-4266141740001234', 'adidas 1', 'adidas shoes', 'very nice adidas shoes', 0, 150.00, 30, '2024-06-11 11:00:00', '2024-06-13 11:00:00', '2024-06-14 11:00:00', 'This is adidas shoes', '423e4567-e89b-12d3-a456-4266141740001234');
-- Inserting more products

INSERT INTO `category` (`uuid_category`, `title`, `meta_title`, `slug`, `content`)
VALUES 
('723e4567-e89b-12d3-a456-4266141740001234', 'Running Shoes', 'Best Running Shoes', 'running-shoes', 'A collection of the best running shoes.'),
('823e4567-e89b-12d3-a456-4266141740001234', 'Casual Shoes', 'Top Casual Shoes', 'casual-shoes', 'A collection of stylish casual shoes.');

INSERT INTO `product_category` (`uuid_product`, `uuid_category`)
VALUES 
('523e4567-e89b-12d3-a456-4266141740001234', '723e4567-e89b-12d3-a456-4266141740001234'),
('623e4567-e89b-12d3-a456-4266141740001234', '823e4567-e89b-12d3-a456-4266141740001234');

INSERT INTO `attribute` (`uuid_attribute`, `key`, `created_date`, `updated_date`)
VALUES 
('923e4567-e89b-12d3-a456-4266141740001234', 'Color', '2024-06-12 12:00:00', '2024-06-13 12:00:00'),
('a23e4567-e89b-12d3-a456-4266141740001234', 'Size', '2024-06-12 12:00:00', '2024-06-13 12:00:00');

INSERT INTO `product_attribute` (`uuid_attribute`, `uuid_product`, `value`)
VALUES 
('923e4567-e89b-12d3-a456-4266141740001234', '523e4567-e89b-12d3-a456-4266141740001234', 'Red'),
('a23e4567-e89b-12d3-a456-4266141740001234', '623e4567-e89b-12d3-a456-4266141740001234', '10');

