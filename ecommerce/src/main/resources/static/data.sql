use nest_ecom;

select * from user;
select * from shop;
select * from role;
select * from user_role;
select * from attribute;
select * from user_address;
select * from shop_address;
select * from shop;
select * from cancel_order_reason;
select * from product;
select * from `order`;
select * from `order_item`;
select * from voucher_redemption;
select * from product_voucher;
select * from voucher where is_visible = true;
-- find vouchers and their constraint for a shop where voucher is claimed by the current user
select p.title, p.uuid_product, COUNT(oi.quantity) as unitsSold, SUM(oi.price * oi.quantity) as revenue
FROM product p
JOIN order_item oi on p.uuid_product = oi.uuid_product
WHERE p.uuid_shop = 'bed361f3-5a86-42eb-ad4e-9cd7d4040fbd'
group by p.uuid_product
