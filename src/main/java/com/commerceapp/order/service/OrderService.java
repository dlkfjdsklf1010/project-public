package com.commerceapp.order.service;

import com.commerceapp.admin.dto.AdminLoginSession;
import com.commerceapp.admin.entity.Admin;
import com.commerceapp.admin.repository.AdminRepository;
import com.commerceapp.customer.entity.Customer;
import com.commerceapp.customer.repository.CustomerRepository;
import com.commerceapp.order.dto.*;
import com.commerceapp.order.entity.Order;
import com.commerceapp.order.entity.OrderItem;
import com.commerceapp.order.enums.OrderStatus;
import com.commerceapp.order.repository.OrderRepository;
import com.commerceapp.product.entity.Product;
import com.commerceapp.product.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final AdminRepository adminRepository;

    /**
     * 주문 생성
     * 검증 + 재고 처리는 Product에서 책임
     */
    @Transactional
    public OrderResponse createOrder(OrderCreateRequest request, HttpSession session) {
        if (session == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("해당 고객 정보가 없습니다."));

        Admin admin = null;

        Order order = Order.create(customer, admin, generateOrderNumber());

        request.getItemList().forEach(itemRequest -> {
            if (itemRequest.getQuantity() < 1) {
                throw new IllegalArgumentException("수량은 1개 이상이어야 합니다.");
            }

            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("선택한 상품이 존재하지 않습니다."));

            product.decreateStock(itemRequest.getQuantity());
            OrderItem orderItem = OrderItem.create(product, itemRequest.getQuantity());
            order.addOrderItem(orderItem);
        });

        return new OrderResponse(orderRepository.save(order));
    }

    // CS 주문
    public OrderResponse createOrderByAdmin(OrderCreateByAdminRequest request, HttpSession session) {
        AdminLoginSession loginAdmin = (AdminLoginSession) session.getAttribute("loginAdmin");

        if (loginAdmin == null) {
            throw new IllegalArgumentException("관리자 로그인이 필요합니다.");
        }

        Admin admin = adminRepository.findById(loginAdmin.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 관리자 정보가 없습니다."));

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("해당 고객 정보가 없습니다."));

        Order order = Order.create(customer, admin, generateOrderNumber());

        request.getItemList().forEach(itemRequest -> {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));

            product.decreateStock(itemRequest.getQuantity());
            OrderItem orderItem = OrderItem.create(product, itemRequest.getQuantity());
            order.addOrderItem(orderItem);
        });

        return new OrderResponse(orderRepository.save(order));
    }

    // 주문 리스트 조회
    @Transactional(readOnly = true)
    public OrderPageResponse getOrders(
            String keyword,
            int page,
            int size,
            String sortBy,
            String direction,
            OrderStatus status
    ) {
        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by(Sort.Direction.fromString(direction), sortBy)
        );

        Page<Order> result = orderRepository.search(keyword, status, pageable);

        return OrderPageResponse.from(result);
    }

    // 주문 상세 조회
    @Transactional(readOnly = true)
    public OrderDetailResponse getOrderDetail(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("상품 주문 내역이 없습니다."));

        return new OrderDetailResponse(order);
    }

    // 주문 상태 변경
    @Transactional
    public void updateStatus(Long orderId, AdminLoginSession adminSession) {
        if (adminSession == null) {
            throw new IllegalArgumentException("관리자만 상태 변경이 가능합니다.");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("상품 주문 내역이 없습니다."));

        order.updateStatus();
    }

    // 주문 취소
    @Transactional
    public void cancelOrder(Long orderId, String reason) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("상품 주문 내역이 없습니다."));

        // 재고 복구
        order.getOrderItemList().forEach(item -> {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));

            product.increaseStock(item.getQuantity());
        });

        order.cancel(reason);
    }

    // 주문 번호 자동 생성 (날짜 + 순번)
    private String generateOrderNumber() {
        LocalDate today = LocalDate.now();

        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.atTime(23, 59, 59);

        long count = orderRepository.countOrdersBetween(start, end);
        long sequence = count + 1;
        String date = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        return date + "-" + String.format("%04d", sequence);
    }
}