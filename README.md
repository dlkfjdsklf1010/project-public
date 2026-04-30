# 주문 관리 시스템 (Commerce Order API)

## 📖 목차

1. [프로젝트 소개](#프로젝트-소개)
2. [팀소개](#팀소개)
3. [프로젝트 계기](#프로젝트-계기)
4. [주요기능](#주요기능)
5. [개발기간](#개발기간)
6. [기술스택](#기술스택)
7. [서비스 구조](#서비스-구조)
8. [와이어프레임](#와이어프레임)
9. [API 명세서](#API-명세서)
10. [ERD](#ERD)
11. [프로젝트 파일 구조](#프로젝트-파일-구조)
12. [Trouble Shooting](#trouble-shooting)

---

## 👨‍🏫 프로젝트 소개




---

## 팀소개

* 10팀
* 박정원 
* 강재구
* 최준영
* 정지수
* 이지영
---

## 프로젝트 계기




---

## 주요기능

* 
* 
* 
* 
* 
* 

---

## ⏲️ 개발기간

* 2026.04.23(목) ~ 2024.04.30(목)

---

## 📚️ 기술스택

### ✔️ Language

* Java

### ✔️ Version Control

* Git / GitHub

### ✔️ IDE

* IntelliJ IDEA

### ✔️ Framework

* Spring Boot
* Spring Data JPA

### ✔️ Deploy

* Local 환경

### ✔️ DBMS

* MySQL

---

## 서비스 구조

* Controller → Service → Repository 구조
* JPA 기반 ORM 사용
* 주문(Order)과 주문상품(OrderItem) 관계 설계
* 세션 기반 인증 (관리자 / 사용자 구분)

---

## 와이어프레임

* (생략 또는 이미지 추가 가능)

---

## API 명세서

주요 API:

*
* 
* 
* 
* 
*

---

## ERD

* Customer (고객)
* Admin (관리자)
* Product (상품)
* Order (주문)
* OrderItem (주문상품)

관계:

* Customer 1 : N Order
* Order 1 : N OrderItem
* Product 1 : N OrderItem

---

## 프로젝트 파일 구조
```plaintext
src
 ┣ main
 ┃ ┣ java
 ┃ ┃ ┗ com.commerceapp
 ┃ ┃
 ┃ ┃   ┣ admin
 ┃ ┃   ┃ ┣ controller
 ┃ ┃   ┃ ┃ ┗ AdminController.java
 ┃ ┃   ┃ ┣ dto
 ┃ ┃   ┃ ┃ ┣ AdminDetailResponse.java
 ┃ ┃   ┃ ┃ ┣ AdminListResponse.java
 ┃ ┃   ┃ ┃ ┣ AdminLoginRequest.java
 ┃ ┃   ┃ ┃ ┣ AdminLoginSession.java
 ┃ ┃   ┃ ┃ ┣ AdminMyPasswordUpdateRequest.java
 ┃ ┃   ┃ ┃ ┣ AdminMyProfileUpdateRequest.java
 ┃ ┃   ┃ ┃ ┣ AdminPageResponse.java
 ┃ ┃   ┃ ┃ ┣ AdminProfileResponse.java
 ┃ ┃   ┃ ┃ ┣ AdminRejectReasonRequest.java
 ┃ ┃   ┃ ┃ ┣ AdminRoleUpdateRequest.java
 ┃ ┃   ┃ ┃ ┣ AdminSignupRequest.java
 ┃ ┃   ┃ ┃ ┣ AdminStatusUpdateRequest.java
 ┃ ┃   ┃ ┃ ┗ AdminUpdateRequest.java
 ┃ ┃   ┃ ┣ entity
 ┃ ┃   ┃ ┃ ┗ Admin.java
 ┃ ┃   ┃ ┣ enums
 ┃ ┃   ┃ ┃ ┣ AdminRole.java
 ┃ ┃   ┃ ┃ ┗ AdminStatus.java
 ┃ ┃   ┃ ┣ repository
 ┃ ┃   ┃ ┃ ┗ AdminRepository.java
 ┃ ┃   ┃ ┗ service
 ┃ ┃   ┃   ┗ AdminService.java
 ┃ ┃
 ┃ ┃   ┣ common
 ┃ ┃   ┃ ┣ config
 ┃ ┃   ┃ ┃ ┣ GlobalExceptionHandler.java
 ┃ ┃   ┃ ┃ ┗ PasswordEncoder.java
 ┃ ┃   ┃ ┣ dto
 ┃ ┃   ┃ ┣ entity
 ┃ ┃   ┃ ┃ ┗ BaseEntity.java
 ┃ ┃   ┃ ┣ exception
 ┃ ┃   ┃ ┃ ┣ ForbiddenException.java
 ┃ ┃   ┃ ┃ ┣ NotFoundException.java
 ┃ ┃   ┃ ┃ ┗ UnauthorizedException.java
 ┃ ┃   ┃ ┣ init
 ┃ ┃   ┃ ┃ ┗ DataInitializer.java
 ┃ ┃   ┃ ┗ repository
 ┃ ┃
 ┃ ┃   ┣ customer
 ┃ ┃   ┃ ┣ controller
 ┃ ┃   ┃ ┃ ┗ CustomerController.java
 ┃ ┃   ┃ ┣ dto
 ┃ ┃   ┃ ┃ ┣ CustomerDeleteResponse.java
 ┃ ┃   ┃ ┃ ┣ CustomerDetailResponse.java
 ┃ ┃   ┃ ┃ ┣ CustomerListResponse.java
 ┃ ┃   ┃ ┃ ┣ CustomerLoginRequest.java
 ┃ ┃   ┃ ┃ ┣ CustomerLoginSession.java
 ┃ ┃   ┃ ┃ ┣ CustomerPageResponse.java
 ┃ ┃   ┃ ┃ ┣ CustomerSignupRequest.java
 ┃ ┃   ┃ ┃ ┣ CustomerStatusRequest.java
 ┃ ┃   ┃ ┃ ┗ CustomerUpdateRequest.java
 ┃ ┃   ┃ ┣ entity
 ┃ ┃   ┃ ┃ ┗ Customer.java
 ┃ ┃   ┃ ┣ enums
 ┃ ┃   ┃ ┃ ┗ CustomerStatus.java
 ┃ ┃   ┃ ┣ repository
 ┃ ┃   ┃ ┃ ┗ CustomerRepository.java
 ┃ ┃   ┃ ┗ service
 ┃ ┃   ┃   ┗ CustomerService.java
 ┃ ┃
 ┃ ┃   ┣ order
 ┃ ┃   ┃ ┣ controller
 ┃ ┃   ┃ ┃ ┗ OrderController.java
 ┃ ┃   ┃ ┣ dto
 ┃ ┃   ┃ ┃ ┣ OrderCreateByAdminRequest.java
 ┃ ┃   ┃ ┃ ┣ OrderCreateRequest.java
 ┃ ┃   ┃ ┃ ┣ OrderDetailResponse.java
 ┃ ┃   ┃ ┃ ┣ OrderGroupedResponse.java
 ┃ ┃   ┃ ┃ ┣ OrderItemDto.java
 ┃ ┃   ┃ ┃ ┣ OrderItemRequest.java
 ┃ ┃   ┃ ┃ ┣ OrderItemResponse.java
 ┃ ┃   ┃ ┃ ┣ OrderPageResponse.java
 ┃ ┃   ┃ ┃ ┗ OrderResponse.java
 ┃ ┃   ┃ ┣ entity
 ┃ ┃   ┃ ┃ ┣ Order.java
 ┃ ┃   ┃ ┃ ┗ OrderItem.java
 ┃ ┃   ┃ ┣ enums
 ┃ ┃   ┃ ┃ ┗ OrderStatus.java
 ┃ ┃   ┃ ┣ repository
 ┃ ┃   ┃ ┃ ┗ OrderRepository.java
 ┃ ┃   ┃ ┗ service
 ┃ ┃   ┃   ┗ OrderService.java
 ┃ ┃
 ┃ ┃   ┣ product
 ┃ ┃   ┃ ┣ controller
 ┃ ┃   ┃ ┃ ┗ ProductController.java
 ┃ ┃   ┃ ┣ dto
 ┃ ┃   ┃ ┃ ┣ ProductCreateRequest.java
 ┃ ┃   ┃ ┃ ┣ ProductDetailResponse.java
 ┃ ┃   ┃ ┃ ┣ ProductListResponse.java
 ┃ ┃   ┃ ┃ ┣ ProductPageResponse.java
 ┃ ┃   ┃ ┃ ┣ ProductResponse.java
 ┃ ┃   ┃ ┃ ┣ ProductUpdateRequest.java
 ┃ ┃   ┃ ┃ ┗ ProductUpdateStatusRequest.java
 ┃ ┃   ┃ ┣ entity
 ┃ ┃   ┃ ┃ ┗ Product.java
 ┃ ┃   ┃ ┣ enums
 ┃ ┃   ┃ ┃ ┗ ProductStatus.java
 ┃ ┃   ┃ ┣ repository
 ┃ ┃   ┃ ┃ ┗ ProductRepository.java
 ┃ ┃   ┃ ┗ service
 ┃ ┃   ┃   ┗ ProductService.java
```
---

## ⚙️ 어떻게 작동하는가

1. 사용자가 주문 요청을 보냄
2. 고객 및 상품 정보 조회
3. 상품 재고 감소 처리
4. Order 및 OrderItem 생성
5. 주문 저장
6. 주문 상태 관리 및 조회 가능

---

## 💡 기술적 의사결정

* **DTO 분리 설계**
  → Entity 직접 노출 방지 및 유지보수성 향상

* **Service 레이어 중심 비즈니스 로직**
  → 역할 분리 및 코드 가독성 향상

* **Enum 기반 상태 관리**
  → 주문 상태 흐름을 안전하게 제어

* **JPA 연관관계 활용**
  → 객체지향적인 데이터 관리

---

## 🔧 Trouble Shooting

### 1. 주문 상태 변경 로직

* 문제: 상태가 무분별하게 변경될 수 있음
* 해결: Enum 기반 순차 변경 로직 구현

---

### 2. 주문 취소 시 재고 복구

* 문제: 취소 시 재고가 복구되지 않는 문제
* 해결: OrderItem을 순회하며 재고 증가 처리

---

### 3. DTO 설계 문제

* 문제: Entity 직접 반환 시 보안 및 유지보수 문제 발생
* 해결: Response DTO 별도 생성

---

### 4. 예외 처리

* 문제: 잘못된 요청 시 명확한 메시지 부족
* 해결: IllegalArgumentException 기반 예외 처리

---
