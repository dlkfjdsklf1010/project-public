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
public class SuperAdminInitializer {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initSuperAdmin() {
        String superAdminEmail = "rkdworn@gmail.com";
        Admin superAdmin = new Admin(
                    "슈퍼관리자",
                    superAdminEmail,
                    passwordEncoder.encode("12345678"),
                    "010-1234-5678",
                    "슈퍼"
            );
        superAdmin.activate(LocalDateTime.now());
        adminRepository.save(superAdmin);
            log.info(" 테스트용 슈퍼 관리자 계정 생성 및 활성화 완료. (Email: {})", superAdminEmail);
        }
}

