package com.commerceapp.common.init;

import com.commerceapp.common.config.PasswordEncoder;
import com.commerceapp.customer.entity.Customer;
import com.commerceapp.customer.repository.CustomerRepository;
import com.commerceapp.product.entity.Product;
import com.commerceapp.product.entity.Product.ProductStatus;
import com.commerceapp.product.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// customer, product 임의 생성
@Slf4j
@Component
@RequiredArgsConstructor
@Profile("local")
public class DataInitializer {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    @Transactional
    public void init() {

        if (customerRepository.count() > 0) return;

        createCustomers();
        createProducts();
    }

    // 👤 고객 20명 생성
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
        log.info("고객 20명 생성 및 활성화 완료.");
    }

    // 📦 상품 100개 생성 (카테고리 포함)
    private void createProducts() {

        String[] names = {"노트북", "키보드", "마우스", "모니터", "헤드셋"};
        String[] categories = {"전자기기", "주변기기", "악세서리"};

        Random random = new Random();

        List<Product> products = new ArrayList<>();

        for (int i = 1; i <= 100; i++) {

            String name = names[random.nextInt(names.length)] + " " + i;
            String category = categories[random.nextInt(categories.length)];

            int price = (random.nextInt(50) + 1) * 10000;
            int stock = random.nextInt(20);

            ProductStatus status;

            int rand = random.nextInt(10);

            if (rand < 7) {
                status = ProductStatus.ON_SALE;
            } else if (rand < 9) {
                status = ProductStatus.SOLD_OUT;
            } else {
                status = ProductStatus.DISCONTINUED;
            }

            Product product = Product.create(name, category, price, stock, status);
            products.add(product);
        }

        productRepository.saveAll(products);
        log.info("상품 100개 생성 및 활성화 완료. (랜덤으로 품절, 단종 있음)");
    }
}