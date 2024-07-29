use nest_ecom;

select * from user;
select * from shop;
select * from role;
select * from user_role;
select * from attribute;
select * from user_address;
select * from shop_address;
select * from shop;
select * from voucher;
delete from product;
select * from `order`;
-- delete  from user_address;
-- delete  from shop_address;
alter table `order` drop column tax;