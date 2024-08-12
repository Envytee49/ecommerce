use nest_ecom;

select * from user where uuid_user = null;
select * from shop;
select * from shop_address;
select * from role;
select * from user_role;
select * from attribute;
select * from product_attribute;
select * from user_address;
select * from cart_item;
select * from default_user_address;
select * from shop_address;
select * from shop;
select * from voucher_info;
select * from cancel_order_reason;
select * from product;
select * from product_review;
select * from category;
select * from `order`;
select * from `order_item`;
select * from sku;
select * from voucher_redemption where uuid_voucher = '5453e468-73a7-42e4-9880-b825de5300af';
select * from product_voucher ;
select * from shop_voucher where uuid_voucher = '5453e468-73a7-42e4-9880-b825de5300af';
select * from voucher_info where uuid_voucher_info = '35621fc2-533f-11ef-bf12-4cd57760a4cc46df';
select * from platform_voucher;
select * from voucher_constraint;
select * from cancelled_order;


SELECT s1_0.uuid_sku, s1_0.price, s1_0.quantity, s1_0.sku, s1_0.uuid_product
FROM sku s1_0
WHERE s1_0.uuid_sku = (SELECT * FROM sku_product_variant_option spvo1_0
WHERE spvo1_0.uuid_product_variant IN ('3e7c5487-c30d-46c3-9a20-47a60b55b998', '3e7c5487-c30d-46c3-9a20-47a60b55b998')
AND spvo1_0.uuid_product_variant_option IN ('08a022d4-c816-44a3-b55b-68d262397ba1', '8ea08453-ca4a-4779-b782-fccc4db60105')
GROUP BY spvo1_0.uuid_sku 
order by COUNT(spvo1_0.uuid_sku)
LIMIT 1
)

