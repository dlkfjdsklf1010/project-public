package com.commerceapp.customer.service;

import com.commerceapp.common.config.PasswordEncoder;
import com.commerceapp.common.exception.NotFoundException;
import com.commerceapp.customer.dto.CustomerLoginRequest;
import com.commerceapp.customer.dto.CustomerSignupRequest;
import com.commerceapp.customer.entity.Customer;
import com.commerceapp.customer.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 고객 관리 Service 클래스
 * 고객 목록 조회, 상세 조회, 정보 수정, 상태 변경, 삭제 비즈니스 로직을 처리합니다.
 */
@Service
public class CustomerService {

    /*========== 속성 ===========*/

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    /*========== 생성자 ===========*/

    /* 생성자 주입 방식으로 CustomerRepository 의존성 주입 */
    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /*========== 기능 ===========*/

    /**
     * 고객 목록 조회
     * 키워드 검색, 상태 필터, 페이징, 정렬을 지원합니다.
     * 소프트 딜리트된 고객은 조회에서 제외됩니다.
     * @param keyword 검색 키워드 (이름 또는 이메일, null이면 전체 조회)
     * @param status 상태 필터 (ACTIVE/INACTIVE/SUSPENDED, 빈 문자열이면 전체 조회)
     * @param page 페이지 번호 (1부터 시작)
     * @param size 페이지당 개수
     * @param sortBy 정렬 기준 필드명
     * @param sortOrder 정렬 순서 (asc/desc)
     * @return 페이징된 고객 목록
     */
    @Transactional(readOnly = true)
    public Page<Customer> getCustomers(String keyword, String status, int page, int size, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        /* null 방어 처리 */
        String keywordParam = (keyword == null) ? "" : keyword;
        String statusParam = (status == null || status.isEmpty()) ? null : status;

        Pageable pageable = PageRequest.of(page - 1, size, sort); /* page는 0부터 시작하므로 -1 처리 */

        return customerRepository.findByKeywordAndStatus(keywordParam, statusParam, pageable);
    }

    /**
     * 고객 단건 조회
     * 존재하지 않는 ID 또는 소프트 딜리트된 고객 조회 시 NotFoundException 발생
     * @param id 고객 고유 식별자
     * @return 고객 엔티티
     */
    @Transactional(readOnly = true)
    public Customer getCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 고객이 존재하지 않습니다."));

        /* 소프트 딜리트된 고객 접근 방어 */
        if (customer.getIsDeleted()) {
            throw new NotFoundException("삭제 된 고객입니다.");
        }

        return customer;
    }

    /**
     * 고객 정보 수정
     * 이름, 이메일, 전화번호를 수정합니다.
     * @Transactional 환경에서 더티체킹으로 자동 UPDATE 쿼리 실행
     * @param id 고객 고유 식별자
     * @param name 수정할 이름
     * @param email 수정할 이메일
     * @param phoneNumber 수정할 전화번호
     * @return 수정된 고객 엔티티
     */
    @Transactional
    public Customer updateCustomer(Long id, String name, String email, String phoneNumber) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 고객이 존재하지 않습니다."));
        customer.updateCustomers(name, email, phoneNumber);
        return customer;
    }

    /**
     * 고객 상태 변경
     * 상태값: ACTIVE(활성), INACTIVE(비활성), SUSPENDED(정지)
     * @param id 고객 고유 식별자
     * @param status 변경할 상태값
     * @return 상태가 변경된 고객 엔티티
     */
    @Transactional
    public Customer updateStatus(Long id, String status) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 고객이 존재하지 않습니다."));
        customer.updateStatus(status);
        return customer;
    }

    /**
     * 고객 소프트 딜리트
     * DB에서 실제로 삭제하지 않고 isDeleted 플래그를 true로 변경합니다.
     * @param id 고객 고유 식별자
     * @return 삭제된 고객 엔티티
     */
    @Transactional
    public Customer deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 고객이 없습니다"));
        customer.delete();
        return customer;
    }

    @Transactional
    public void signUp(CustomerSignupRequest request) {
        // 1. 이메일 중복 확인
        if(customerRepository.existsByEmail(request.getEmail())) {
           throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }

        // 2. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 3. 고객 객체 생성 및 저장
        Customer customer = new Customer(
                request.getName(),
                request.getEmail(),
                encodedPassword,
                request.getPhoneNumber()
        );
        customerRepository.save(customer);
    }

    @Transactional(readOnly = true)
    public Customer login(CustomerLoginRequest request) {
        // 1. 이메일로 고객 조회
        Customer customer = customerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다."));

        // 2. 비밀번호 확인
        if(!passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        // 3. 고객 반환
        return customer;
    }
}
