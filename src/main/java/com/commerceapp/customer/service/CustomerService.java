package com.commerceapp.customer.service;
import com.commerceapp.customer.enums.CustomerStatus;

import com.commerceapp.common.config.PasswordEncoder;
import com.commerceapp.common.exception.NotFoundException;
import com.commerceapp.customer.dto.CustomerLoginRequest;
import com.commerceapp.customer.dto.CustomerLoginSession;
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
    public Page<Customer> getCustomerList(String keyword, String status, int page, int size, String sortBy, String sortOrder) {
        // 1. 정렬 방향 설정
        // - sortOrder가 "desc"면 내림차순, 그 외엔 오름차순
        Sort sort = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        // 2. keyword null 방어 처리
        // - keyword가 null로 오면 빈 문자열로 변환하여 NPE 방지
        String keywordParam = (keyword == null) ? "" : keyword;

        // 3. status 빈 문자열 방어 처리
        // - status가 빈 문자열이면 null로 변환하여 전체 조회
        // - null이면 @Query에서 (:status IS NULL) 조건으로 전체 조회
        CustomerStatus statusParam = (status == null || status.isEmpty()) ? null : CustomerStatus.valueOf(status);

        // 4. 페이징 객체 생성
        // - JPA는 페이지 번호가 0부터 시작하므로 -1 처리
        Pageable pageable = PageRequest.of(page - 1, size, sort); /* page는 0부터 시작하므로 -1 처리 */

        // 5. DB 조회 후 반환
        // - 소프트 딜리트된 고객(isDeleted = true) 자동 제외
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
        // 1. ID로 고객 조회
        // - 존재하지 않는 ID면 NotFoundException 발생 → 404 반환
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 고객이 존재하지 않습니다."));

        // 2. 소프트 딜리트된 고객 접근 방어
        // - isDeleted = true면 삭제된 고객이므로 NotFoundException 발생
        if (customer.getIsDeleted()) {
            throw new NotFoundException("삭제된 고객입니다.");
        }

        // 3. 고객 반환
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
        // 1. ID로 고객 조회
        // - 존재하지 않는 ID면 NotFoundException 발생
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 고객이 존재하지 않습니다."));

        // 2. 이메일 중복 확인
        // - 변경하려는 이메일이 현재 이메일과 다르고, 이미 사용중인 이메일이면 예외 발생
        if(!customer.getEmail().equals(email) && customerRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }

        // 3. 고객 정보 수정
        // - @Transactional 환경에서 더티체킹으로 자동 UPDATE 쿼리 실행
        // - save() 호출 없이도 자동으로 DB에 반영됨
        customer.updateCustomers(name, email, phoneNumber);

        // 3. 수정된 고객 반환
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
    public Customer updateStatus(Long id, CustomerStatus status) {
        // 1. ID로 고객 조회
        // - 존재하지 않는 ID면 NotFoundException 발생
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 고객이 존재하지 않습니다."));

        // 2. 고객 상태 변경
        // - CustomerStatus Enum을 그대로 전달
        customer.updateStatus(status);

        // 3. 상태가 변경된 고객 반환
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
        // 1. ID로 고객 조회
        // - 존재하지 않는 ID면 NotFoundException 발생
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 고객이 없습니다"));

        // 2. 소프트 딜리트 처리
        // - DB에서 실제로 삭제하지 않고 isDeleted = true로 변경
        // - 더티체킹으로 자동 UPDATE 쿼리 실행
        customer.delete();

        // 3. 삭제된 고객 반환
        return customer;
    }

    @Transactional
    public void signUpCustomer(CustomerSignupRequest request) {
        // 1. 이메일 중복 확인
        // - DB에 동일한 이메일이 있으면 IllegalArgumentException 발생
        if(customerRepository.existsByEmail(request.getEmail())) {
           throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }

        // 2. 비밀번호 암호화
        // - BCrypt 알고리즘으로 비밀번호를 암호화하여 DB에 평문 저장 방지
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 3. 고객 객체 생성
        // - 암호화된 비밀번호로 Customer 객체 생성
        // - 기본 상태는 ACTIVE로 설정됨
        Customer customer = new Customer(
                request.getName(),
                request.getEmail(),
                encodedPassword,
                request.getPhoneNumber()
        );

        // 4. DB에 저장
        customerRepository.save(customer);
    }

    @Transactional(readOnly = true)
    public CustomerLoginSession loginCustomer(CustomerLoginRequest request) {
        // 1. 이메일로 고객 조회
        // - 이메일이 존재하지 않으면 예외 발생
        // - 보안을 위해 "이메일이 없습니다" 대신 "이메일 또는 비밀번호가 일치하지 않습니다" 반환
        //   → 이메일 존재 여부를 노출하지 않아 보안 강화

        Customer customer = customerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다."));

        // 2. 비밀번호 확인
        // - 입력한 비밀번호와 DB에 암호화된 비밀번호를 BCrypt로 비교
        // - 일치하지 않으면 예외 발생
        if(!passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        // 3. 고객 반환
        return CustomerLoginSession.from(customer);
    }
}
