package com.commerceapp.admin.service;

import com.commerceapp.admin.dto.AdminSignupRequest;
import com.commerceapp.admin.entity.Admin;
import com.commerceapp.admin.repository.AdminRepository;
import com.commerceapp.common.config.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(AdminSignupRequest request){
        if (adminRepository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Admin admin = new Admin(
                request.getName(),
                request.getEmail(),
                encodedPassword,
                request.getPhoneNumber(),
                request.getRole()
        );
        adminRepository.save(admin);
    }

}
