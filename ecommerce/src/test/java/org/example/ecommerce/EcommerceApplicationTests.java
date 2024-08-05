package org.example.ecommerce;

import org.example.ecommerce.common.constants.DiscountType;
import org.example.ecommerce.common.constants.Role;
import org.example.ecommerce.common.constants.VoucherType;
import org.example.ecommerce.order.model.CancelledOrderReason;
import org.example.ecommerce.order.repository.CancelledOrderReasonRepository;
import org.example.ecommerce.product.model.*;
import org.example.ecommerce.product.repository.AttributeRepository;
import org.example.ecommerce.product.repository.CategoryRepository;
import org.example.ecommerce.product.repository.ProductRepository;
import org.example.ecommerce.shop.model.Shop;
import org.example.ecommerce.shop.model.ShopAddress;
import org.example.ecommerce.shop.repository.ShopAddressRepository;
import org.example.ecommerce.shop.repository.ShopRepository;
import org.example.ecommerce.user.model.*;
import org.example.ecommerce.user.repository.*;
import org.example.ecommerce.voucher.model.ProductVoucher;
import org.example.ecommerce.voucher.model.Voucher;
import org.example.ecommerce.voucher.model.VoucherConstraint;
import org.example.ecommerce.voucher.repository.VoucherConstraintRepository;
import org.example.ecommerce.voucher.repository.ProductVoucherRepository;
import org.example.ecommerce.voucher.repository.UserRoleRepository;
import org.example.ecommerce.voucher.repository.VoucherRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class EcommerceApplicationTests {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AttributeRepository attributeRepository;
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private UserAddressRepository userAddressRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ShopAddressRepository shopAddressRepository;
    @Autowired
    private VoucherConstraintRepository voucherConstraintRepository;
    @Autowired
    private ProductVoucherRepository productVoucherRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private CancelledOrderReasonRepository cancelledOrderReasonRepository;

    @Test
    public void addUser() {

        User user5 = User.builder()
                .username("hahaha")
                .mobile("1234567890")
                .email("user2@example.com")
                .avatar("avatar1.jpg")
                .description("i am handsome")
                .password(passwordEncoder.encode("123456"))
                .registerDate(LocalDateTime.parse("2024-06-20T12:00:00"))
                .lastLogin(LocalDateTime.parse("2024-06-21T12:00:00"))
                .activate(1)
                .build();
        var userRole5 = UserRole.builder()
                .uuidUser(user5.getUuidUser())
                .role(Role.USER)
                .build();
        User user1 = User.builder()
                .username("mens.store")
                .mobile("1234567890")
                .email("seller1@example.com")
                .avatar("avatar1.jpg")
                .description("seller 1")
                .password(passwordEncoder.encode("123456"))
                .registerDate(LocalDateTime.parse("2024-06-20T12:00:00"))
                .lastLogin(LocalDateTime.parse("2024-06-21T12:00:00"))
                .activate(1)
                .build();
        var userRole11 = UserRole.builder()
                .uuidUser(user1.getUuidUser())
                .role(Role.SELLER)
                .build();
        var userRole12 = UserRole.builder()
                .uuidUser(user1.getUuidUser())
                .role(Role.USER)
                .build();
        // get role id
        // insert user
        // insert user role
        User user4 = User.builder()
                .username("shoes.store")
                .mobile("1234567890")
                .email("seller2@example.com")
                .avatar("avatar1.jpg")
                .description("seller 2")
                .password(passwordEncoder.encode("123456"))
                .registerDate(LocalDateTime.parse("2024-06-20T12:00:00"))
                .lastLogin(LocalDateTime.parse("2024-06-21T12:00:00"))
                .activate(1)
                .build();
        var userRole41 = UserRole.builder()
                .uuidUser(user4.getUuidUser())
                .role(Role.SELLER)
                .build();
        var userRole42 = UserRole.builder()
                .uuidUser(user4.getUuidUser())
                .role(Role.USER)
                .build();
        User user2 = User.builder()
                .username("admin")
                .mobile("0987654321")
                .email("admin@example.com")
                .avatar("avatar2.jpg")
                .description("admin")
                .password(passwordEncoder.encode("123456"))
                .registerDate(LocalDateTime.parse("2024-06-20T13:00:00"))
                .lastLogin(LocalDateTime.parse("2024-06-21T13:00:00"))
                .activate(0)
                .build();
        var userRole2 = UserRole.builder()
                .uuidUser(user2.getUuidUser())
                .role(Role.ADMIN)
                .build();
        User user3 = User.builder()
                .username("envytee")
                .mobile("3344556677")
                .email("user@example.com")
                .avatar("avatar5.jpg")
                .description("An occasional user")
                .password(passwordEncoder.encode("123456"))
                .registerDate(LocalDateTime.parse("2024-06-20T16:00:00"))
                .lastLogin(LocalDateTime.parse("2024-06-21T16:00:00"))
                .activate(1)
                .build();
        var userRole3 = UserRole.builder()
                .uuidUser(user3.getUuidUser())
                .role(Role.USER)
                .build();
        userRepository.saveAll(List.of(user1, user2, user3, user4, user5));
        userRoleRepository.saveAll(List.of(userRole11, userRole12, userRole41, userRole42, userRole2, userRole3, userRole5));
    }

    @Test
    void addCategory() {
        List<Category> categories = List.of(
                Category.builder()
                        .title("Running Shoes")
                        .metaTitle("Best Running Shoes")
                        .slug("running-shoes")
                        .content("A collection of the best running shoes.")
                        .build(),
                Category.builder()
                        .title("Casual Shoes")
                        .metaTitle("Best Casual Shoes")
                        .slug("casual-shoes")
                        .content("A collection of the best casual shoes.")
                        .build()
        );
        categoryRepository.saveAll(categories);
    }

    @Test
    void addAttribute() {
        List<Attribute> attributes = List.of(
                Attribute.builder().key("Color").build(),
                Attribute.builder().key("Size").build()
        );
        attributeRepository.saveAll(attributes);
    }

    @Test
    public void addShop() {
        String sellerId1 = userRepository.findByEmail("seller1@example.com").get().getUuidUser();
        String sellerId2 = userRepository.findByEmail("seller2@example.com").get().getUuidUser();
        List<Shop> shops = List.of(
                Shop.builder()
                        .name("MEN STORE - HIGH PREMIUM CHINA IMPORTS")
                        .uuidSeller(sellerId1)
                        .build(),
                Shop.builder()
                        .name("SHOES STORE - HIGH PREMIUM ITALIA IMPORTS")
                        .uuidSeller(sellerId2)
                        .build()
        );
        shopRepository.saveAll(shops);
    }

    @Test
    void addProduct() {
        String sellerId1 = userRepository.findByEmail("seller1@example.com").get().getUuidUser();
        String shopId1 = shopRepository.findByUuidSeller(sellerId1).getUuidShop();
        String sellerId2 = userRepository.findByEmail("seller2@example.com").get().getUuidUser();
        String shopId2 = shopRepository.findByUuidSeller(sellerId2).getUuidShop();
        // ========================================================================
        Product product1 = Product.builder()
                .title("nike 1")
                .metaTitle("nike shoes")
                .summary("very nice nike shoes")
                .price(120.99)
                .quantity(50)
                .publishedDate(LocalDateTime.parse("2024-06-21T16:00:00"))
                .description("This is nike shoes")
                .discount(0.5)
                .build();
        ProductCategory productCategory1 = new ProductCategory(product1, categoryRepository.findByTitle("Casual Shoes"));
        product1.setCategories(List.of(productCategory1));
        List<ProductAttribute> productAttribute1 = List.of(
                new ProductAttribute(product1, attributeRepository.findByKey("Color"), "Red"),
                new ProductAttribute(product1, attributeRepository.findByKey("Size"), "Big")

        );
        product1.setAttributes(productAttribute1);
        product1.setUuidShop(shopId1);
        // ========================================================================
        Product product2 = Product.builder()
                .title("adidas 1")
                .metaTitle("adidas shoes")
                .summary("very nice adidas shoes")
                .price(150)
                .quantity(50)
                .discount(0.1)
                .publishedDate(LocalDateTime.parse("2024-06-21T16:00:00"))
                .description("This is adidas shoes")
                .build();
        ProductCategory productCategory2 = new ProductCategory(product2, categoryRepository.findByTitle("Running Shoes"));
        product2.setCategories(List.of(productCategory2));
        List<ProductAttribute> productAttribute2 = List.of(
                new ProductAttribute(product2, attributeRepository.findByKey("Color"), "Black"),
                new ProductAttribute(product2, attributeRepository.findByKey("Size"), "Small")

        );
        product2.setAttributes(productAttribute2);
        product2.setUuidShop(shopId2);
        // ========================================================================
        Product product3 = Product.builder()
                .title("adidas 2")
                .metaTitle("adidas shoes")
                .summary("very nice adidas shoes")
                .price(180)
                .quantity(50)
                .discount(0.1)
                .publishedDate(LocalDateTime.parse("2024-06-21T16:00:00"))
                .description("This is adidas shoes")
                .build();
        product3.setCategories(List.of(productCategory2));
        List<ProductAttribute> productAttribute3 = List.of(
                new ProductAttribute(product3, attributeRepository.findByKey("Color"), "Pink"),
                new ProductAttribute(product3, attributeRepository.findByKey("Size"), "XXL")

        );
        product3.setAttributes(productAttribute3);
        product3.setUuidShop(shopId1);
        // ========================================================================
        Product product4 = Product.builder()
                .title("nike 2")
                .metaTitle("nike shoes")
                .summary("very nice nike shoes")
                .price(150)
                .quantity(50)
                .discount(0.1)
                .publishedDate(LocalDateTime.parse("2024-06-21T16:00:00"))
                .description("This is nike shoes")
                .build();
        product4.setCategories(List.of(productCategory1));
        List<ProductAttribute> productAttribute4 = List.of(
                new ProductAttribute(product4, attributeRepository.findByKey("Color"), "Red"),
                new ProductAttribute(product4, attributeRepository.findByKey("Size"), "Big")
        );
        product4.setAttributes(productAttribute4);
        product4.setUuidShop(shopId2);
        productRepository.saveAll(List.of(product1, product2, product3, product4));
    }

    @Test
    void addVoucher() {
        String sellerId1 = userRepository.findByEmail("seller1@example.com").get().getUuidUser();
        String shopId1 = shopRepository.findByUuidSeller(sellerId1).getUuidShop();

        String sellerId2 = userRepository.findByEmail("seller2@example.com").get().getUuidUser();
        String shopId2 = shopRepository.findByUuidSeller(sellerId2).getUuidShop();

        var v1 = Voucher.builder()
                .voucherCode("SPPMKPEU0325")
                .voucherName("10% Off")
                .voucherType(VoucherType.PRODUCTS)
                .discountType(DiscountType.PERCENTAGE)
                .discount(0.1)
                .description("Use this voucher to get discount")
                .quantity(2)
                .uuidShop(shopId1)
                .build();
        var v2 = Voucher.builder()
                .voucherCode("SPPMKPEU0327")
                .discountType(DiscountType.PERCENTAGE)
                .voucherType(VoucherType.PRODUCTS)
                .voucherName("5% Off")
                .discount(0.05)
                .description("Use this voucher to get discount")
                .uuidShop(shopId2)
                .quantity(2)
                .build();
        var v3 = Voucher.builder()
                .voucherCode("SPPMKPEU2141")
                .voucherName("DISCOUNT 10$ Off")
                .discountType(DiscountType.FIXED)
                .voucherType(VoucherType.ALL_SHOP)
                .discount(10.0)
                .description("Use this voucher to get discount")
                .quantity(2)
                .uuidShop(shopId1)
                .build();
        var v4 = Voucher.builder()
                .voucherCode("SPPMKPEU0324")
                .voucherName("DISCOUNT 30$ Off")
                .discountType(DiscountType.FIXED)
                .voucherType(VoucherType.ALL_SHOP)
                .discount(30.0)
                .description("Use this voucher to get discount")
                .quantity(2)
                .uuidShop(shopId2)
                .build();
        voucherRepository.saveAll(List.of(v1, v2, v3, v4));
        var pv1 = ProductVoucher.builder()
                .uuidVoucher(v1.getUuidVoucher())
                .uuidProduct(productRepository.findByTitle("nike 1").getUuidProduct())
                .build();
        var pv2 = ProductVoucher.builder()
                .uuidVoucher(v2.getUuidVoucher())
                .uuidProduct(productRepository.findByTitle("adidas 1").getUuidProduct())
                .build();
        productVoucherRepository.saveAll(List.of(pv1, pv2));
        var vc1 = VoucherConstraint.builder()
                .maxUsage(1)
                .minSpend(500)
                .uuidVoucher(v1.getUuidVoucher())
                .validFrom(LocalDateTime.parse("2024-07-21T16:00:00"))
                .validUntil(LocalDateTime.parse("2024-09-10T16:00:00"))
                .build();
        var vc2 = VoucherConstraint.builder()
                .maxUsage(1)
                .minSpend(700)
                .uuidVoucher(v2.getUuidVoucher())
                .validFrom(LocalDateTime.parse("2024-07-21T16:00:00"))
                .validUntil(LocalDateTime.parse("2024-09-10T16:00:00"))
                .build();
        var vc3 = VoucherConstraint.builder()
                .maxUsage(1)
                .minSpend(300)
                .uuidVoucher(v3.getUuidVoucher())
                .validFrom(LocalDateTime.parse("2024-07-21T16:00:00"))
                .validUntil(LocalDateTime.parse("2024-09-10T16:00:00"))
                .build();
        var vc4 = VoucherConstraint.builder()
                .maxUsage(1)
                .minSpend(1000)
                .uuidVoucher(v4.getUuidVoucher())
                .validFrom(LocalDateTime.parse("2024-07-21T16:00:00"))
                .validUntil(LocalDateTime.parse("2024-09-10T16:00:00"))
                .build();
        voucherConstraintRepository.saveAll(List.of(vc1, vc2, vc3, vc4));
    }


    @Test
    void addUserAddress() {
        String userId = userRepository.findByEmail("user@example.com").get().getUuidUser();

        var address1 = new UserAddress();
        address1.setMobile("0123456789");
        address1.setCity("Hanoi");
        address1.setDistrict("Ha Ba Trung");
        address1.setReceiverName("Thuận");
        address1.setStreet("Minh Khai");
        address1.setUuidUser(userId);
        address1.setPostalCode(10010);
        var address2 = new UserAddress();
        address2.setMobile("09876543219");
        address2.setReceiverName("Trang");
        address2.setCity("Hanoi");
        address2.setDistrict("Dong Da");
        address2.setStreet("Dang Van Ngu");
        address2.setUuidUser(userId);
        address2.setPostalCode(10010);
        userAddressRepository.saveAll(List.of(address1, address2));
    }

    @Test
    void addShopAddress() {
        String sellerId1 = userRepository.findByEmail("seller1@example.com").get().getUuidUser();
        String shopId1 = shopRepository.findByUuidSeller(sellerId1).getUuidShop();
        String sellerId2 = userRepository.findByEmail("seller2@example.com").get().getUuidUser();
        String shopId2 = shopRepository.findByUuidSeller(sellerId2).getUuidShop();
        var address1 = new ShopAddress();
        address1.setMobile("0123456789");
        address1.setCity("Hanoi");
        address1.setDistrict("Thanh Xuan");
        address1.setStreet("Van Quan");
        address1.setSellerName("Thắng");
        address1.setUuidShop(shopId1);
        address1.setPostalCode(10010);
        var address2 = new ShopAddress();
        address2.setMobile("09876543219");
        address2.setCity("Hanoi");
        address2.setSellerName("Hiếu");
        address2.setDistrict("Thanh Xuan");
        address2.setStreet("Chien Thang");
        address2.setUuidShop(shopId2);
        address2.setPostalCode(10010);
        shopAddressRepository.saveAll(List.of(address1, address2));
    }

    @Test
    void addReason() {
        cancelledOrderReasonRepository.saveAll(
                List.of(
                        CancelledOrderReason.builder()
                                .reason("Need to change delivery address")
                                .build(),
                        CancelledOrderReason.builder()
                                .reason("Need to input/change voucher code")
                                .build(),
                        CancelledOrderReason.builder()
                                .reason("Need to modify order (size, color, quantity, etc.)")
                                .build(),
                        CancelledOrderReason.builder()
                                .reason("Payment procedure to troublesome")
                                .build(),
                        CancelledOrderReason.builder()
                                .reason("Found cheaper elsewhere")
                                .build(),
                        CancelledOrderReason.builder()
                                .reason("Don't want to buy anymore")
                                .build(),
                        CancelledOrderReason.builder()
                                .reason("Others")
                                .build()

                )
        );
    }
}
