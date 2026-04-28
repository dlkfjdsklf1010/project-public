package com.commerceapp.common.init;

import com.commerceapp.admin.entity.Admin;
import com.commerceapp.admin.enums.AdminRole;
import com.commerceapp.admin.repository.AdminRepository;
import com.commerceapp.common.config.PasswordEncoder;
import com.commerceapp.customer.entity.Customer;
import com.commerceapp.customer.repository.CustomerRepository;
import com.commerceapp.product.entity.Product;
import com.commerceapp.product.enums.ProductStatus;
import com.commerceapp.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// admin, customer, product 임의 생성
@Slf4j
@Component
@Profile({"local", "test"})
@RequiredArgsConstructor
public class DataInitializer {

    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void init() {

        log.info("데이터 초기화 시작");

        // 슈퍼 관리자 생성
        Admin superAdmin = createSuperAdmin();
        createTestAdmins();

        // 고객 생성
        if (customerRepository.count() == 0) {
            createCustomers();
        }

        // 슈퍼 관리자가 만든 상품 생성
        if (productRepository.count() == 0) {
            createProducts(superAdmin);
        }

        log.info("데이터 초기화 완료");
    }

    // 슈퍼 관리자 생성
    private Admin createSuperAdmin() {

        String superAdminEmail = "rkdworn@gmail.com";

        return adminRepository.findByEmail(superAdminEmail)
                .orElseGet(() -> {
                    Admin admin = new Admin(
                            "슈퍼관리자",
                            superAdminEmail,
                            passwordEncoder.encode("12345678"),
                            "010-1234-5678",
                            "슈퍼관리자"
                    );
                    admin.activate(LocalDateTime.now());

                    adminRepository.save(admin);
                    log.info(" 테스트용 슈퍼 관리자 계정 생성 및 활성화 완료. (Email: {})", superAdminEmail);

                    return admin;
                });
    }

    // 테스트 관리자 5명 생성
    private void createTestAdmins() {

        String encodedPassword = passwordEncoder.encode("12345678");
        LocalDateTime now = LocalDateTime.now();

        for (int i = 1; i <= 5; i++) {
            String email = "testadmin" + i + "@gmail.com";

            if (adminRepository.existsByEmail(email)) continue;

            Admin admin = new Admin(
                    "테스트관리자" + i,
                    email,
                    encodedPassword,
                    "010-9999-" + String.format("%04d", i),
                    "운영"
            );

            switch (i) {
                case 1 -> admin.activate(now);
                case 3 -> admin.reject("테스트용 거부 사유", now);
                case 4 -> admin.ban();
                case 5 -> admin.deactivate();
            }

            adminRepository.save(admin);
        }

        log.info("테스트용 운영 관리자 계정 생성 완료 (활성:1, 승인 대기:1, 거부:1, 정지:1, 비활성:1)");
    }

    // 고객 생성
    private void createCustomers() {

        List<Customer> customers = new ArrayList<>();

        for (int i = 1; i <= 20; i++) {

            String phone = String.format("010-%04d-%04d",
                    (int)(Math.random() * 10000),
                    (int)(Math.random() * 10000)
            );

            Customer customer = Customer.create(
                    "고객" + i,
                    "user" + i + "@test.com",
                    passwordEncoder.encode("1234"),
                    phone,
                    "ACTIVE"
            );

            customers.add(customer);
        }

        customerRepository.saveAll(customers);
        log.info("고객 20명 생성 완료");
    }

    // 상품 생성
    private void createProducts(Admin admin) {

        String[] names = {"노트북", "키보드", "마우스", "모니터", "헤드셋"};
        String[] categories = {"전자기기", "주변기기", "악세서리"};

        Random random = new Random();
        List<Product> products = new ArrayList<>();

        for (int i = 1; i <= 50; i++) {

            String name = names[random.nextInt(names.length)] + " " + i;
            String category = categories[random.nextInt(categories.length)];

            int price = (random.nextInt(50) + 1) * 10000;
            int stock = random.nextInt(20);

            ProductStatus status;
            int rand = random.nextInt(10);

            if (rand < 7) status = ProductStatus.ON_SALE;
            else if (rand < 9) status = ProductStatus.SOLD_OUT;
            else status = ProductStatus.DISCONTINUED;

            Product product = Product.create(name, category, price, stock, status, admin);

            products.add(product);
        }

        productRepository.saveAll(products);
        log.info("상품 50개 생성 완료. (랜덤으로 품절, 단종 포함)");
    }
}