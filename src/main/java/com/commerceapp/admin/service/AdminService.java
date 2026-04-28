package com.commerceapp.admin.service;

import com.commerceapp.admin.dto.*;
import com.commerceapp.admin.entity.Admin;
import com.commerceapp.admin.enums.AdminRole;
import com.commerceapp.admin.enums.AdminStatus;
import com.commerceapp.admin.repository.AdminRepository;
import com.commerceapp.common.config.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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

    @Transactional(readOnly = true)
    public AdminLoginSession login(AdminLoginRequest request){
        Admin admin = adminRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다.")
        );

        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())){
            throw new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        AdminStatus status = AdminStatus.from(admin.getStatus());
        if (!status.isLogin()) {
            throw new IllegalStateException(status.getMessage());
        }

        return AdminLoginSession.from(admin);
    }

    @Transactional(readOnly = true)
    public AdminPageResponse getAdminList(
            String keyword, AdminRole role, AdminStatus status,
            int page, int size, String sortBy, String direction){
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page -1, size, sort);
        String roleValue = (role != null) ? role.getDatabaseValue() : null;
        String statusValue = (status != null) ? status.getDatabaseValue() : null;
        Page<Admin> adminPage = adminRepository.searchAdmins(keyword, roleValue, statusValue,pageable);

        return AdminPageResponse.from(adminPage);
    }

    @Transactional(readOnly = true)
    public AdminDetailResponse getAdminDetail(Long adminId){
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 관리자입니다.")
        );
        return AdminDetailResponse.from(admin);

    }

    @Transactional(readOnly = true)
    public AdminProfileResponse getMyProfile(Long adminId){
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 관리자입니다.")
        );
        return AdminProfileResponse.from(admin);
    }

    @Transactional
    public void adminUpdate(Long adminId, AdminUpdateRequest request){
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 관리자입니다.")
        );
        if (adminRepository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }
        admin.update(
                request.getName(),
                request.getEmail(),
                request.getPhoneNumber()
        );

    }

    @Transactional
    public void updateMyProfile(Long adminId, AdminMyProfileUpdateRequest request){
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 관리자입니다.")
        );
        admin.update(
                request.getName(),
                request.getEmail(),
                request.getPhoneNumber()
        );
    }

    @Transactional
    public void updateMyPassword(Long adminId, AdminMyPasswordUpdateRequest request){
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 관리자입니다.")
        );
        admin.updateMyPassword(
                request.getPassword()
        );
    }

    @Transactional
    public void changeAdminRole(Long adminId, AdminRoleUpdateRequest request){
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 관리자입니다.")
        );

        admin.changeRole(request.getRole().getDatabaseValue());
    }

    @Transactional
    public void changeAdminStatus(Long adminId, AdminStatusUpdateRequest request){
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 관리자입니다.")
        );
        if (request.getStatus() == AdminStatus.BANNED){
            admin.ban();
        } else if (request.getStatus() == AdminStatus.DEACTIVATE){
            admin.deactivate();
        } else {
            throw new IllegalArgumentException("잘못된 상태 변경 요청입니다.");
        }
    }

    @Transactional
    public void approveAdmin(Long adminId){
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 관리자입니다.")
        );
        if (!AdminStatus.PENDING.getDatabaseValue().equals(admin.getStatus())){
            throw new IllegalStateException("승인대기 상태인 관리자만 거부할 수 있습니다.");
        }

        admin.activate(LocalDateTime.now());
    }

    @Transactional
    public void rejectAdmin(Long adminId, AdminRejectReasonRequest request){
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 관리자입니다.")
        );
        if (!AdminStatus.PENDING.getDatabaseValue().equals(admin.getStatus())){
            throw new IllegalStateException("승인대기 상태인 관리자만 거부할 수 있습니다.");
        }

        admin.reject(request.getRejectReason(), LocalDateTime.now());
    }

    @Transactional
    public void deleteAdmin(Long adminId){
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 관리자입니다.")
        );

        adminRepository.delete(admin);
    }

}
