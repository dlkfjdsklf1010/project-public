package com.commerceapp.admin.service;

import com.commerceapp.admin.entity.Admin;
import com.commerceapp.admin.repository.AdminRepository;
import com.commerceapp.common.config.PasswordEncoder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@Component
@Profile({"local", "test"})
@RequiredArgsConstructor
public class TestAdminInitializer {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initTestAdmins() {
        // 기존 10개에서 5개 생성으로 변경
        log.info("슈퍼관리자 기능 테스트용 운영 관리자 계정 5개 생성을 시작합니다.");

        String encodedPassword = passwordEncoder.encode("12345678");
        LocalDateTime now = LocalDateTime.now();

        for (int i = 1; i <= 5; i++) {
            String email = "testadmin" + i + "@gmail.com";

            // 생성 시점의 기본 상태는 "승인대기(PENDING)"로 설정됨
            Admin testAdmin = new Admin(
                    "테스트관리자" + i,
                    email,
                    encodedPassword,
                    "010-9999-" + String.format("%04d", i),
                    "운영"
            );

            // i 값에 따라 각기 다른 상태로 전이 (State Transition)
            switch (i) {
                case 1:
                    testAdmin.activate(now);
                    log.debug("활성 상태 계정 세팅: {}", email);
                    break;
                case 2:
                    // 생성 기본값이 승인대기이므로 아무 작업도 하지 않음
                    log.debug("승인대기 상태 계정 세팅: {}", email);
                    break;
                case 3:
                    testAdmin.reject("테스트용 거부 사유입니다.", now);
                    log.debug("거부 상태 계정 세팅: {}", email);
                    break;
                case 4:
                    testAdmin.ban();
                    log.debug("정지 상태 계정 세팅: {}", email);
                    break;
                case 5:
                    testAdmin.deactivate();
                    log.debug("비활성 상태 계정 세팅: {}", email);
                    break;
            }

            adminRepository.save(testAdmin);
        }

        log.info("테스트용 운영 관리자 계정 생성 완료 (활성:1, 승인대기:1, 거부:1, 정지:1, 비활성:1)");
    }
}