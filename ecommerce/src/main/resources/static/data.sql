use nest_ecom;

select * from user;
select * from shop;
select * from role;
select * from user_role;
select * from attribute;
select * from user_address;
select * from shop_address;
select * from shop;

select * from product;
select * from `order`;
select * from voucher_redemption;
select * from product_voucher;
select * from voucher;
-- find vouchers and their constraint for a shop where voucher is claimed by the current user

create table `test` (
	`value` decimal(10,2)
)
