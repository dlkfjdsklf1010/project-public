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

    // 회원가입
    @Transactional
    public void adminSignup(AdminSignupRequest request){
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

    // 로그인
    @Transactional(readOnly = true)
    public AdminLoginSession adminLogin(AdminLoginRequest request){
        Admin admin = adminRepository.findByEmailAndIsDeletedFalse(request.getEmail()).orElseThrow(
                () -> new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다.")
        );

        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())){
            throw new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }


        if (!admin.getStatus().isLogin()) {
            throw new IllegalStateException(admin.getStatus().getMessage());
        }

        return AdminLoginSession.from(admin);
    }

    // 관리자 리스트 조회
    @Transactional(readOnly = true)
    public AdminPageResponse getAdminList(
            String keyword, AdminRole role, AdminStatus status,
            int page, int size, String sortBy, String direction){

        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page -1, size, sort);
        String roleValue = (role != null) ? role.getDisplayName() : null;
        String statusValue = (status != null) ? status.getDisplayName() : null;
        Page<Admin> adminPage = adminRepository.searchAdmins(keyword, role, status, pageable);

        return AdminPageResponse.from(adminPage);
    }

    // 관리자 상세 조회
    @Transactional(readOnly = true)
    public AdminDetailResponse getAdminDetail(Long adminId){
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 관리자입니다.")
        );

        if (admin.getIsDeleted()){
            throw new IllegalStateException("삭제된 관리자 입니다.");
        }

        return AdminDetailResponse.from(admin);
    }

    // 관리자 내 프로필 조회
    @Transactional(readOnly = true)
    public AdminProfileResponse getMyProfile(Long adminId){
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 관리자입니다.")
        );

        return AdminProfileResponse.from(admin);
    }

    // 관리자 프로필 수정
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

    // 관리자 내 프로필 수정
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

    // 관리자 내 프로필 비밀번호 수정
    @Transactional
    public void updateMyPassword(Long adminId, AdminMyPasswordUpdateRequest request){
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 관리자입니다.")
        );

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        admin.updateMyPassword(encodedPassword);

    }

    // 관리자 역할 변경
    @Transactional
    public void changeAdminRole(Long adminId, AdminRoleUpdateRequest request){
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 관리자입니다.")
        );

        admin.changeRole(request.getRole());
    }

    // 관리자 상태 변경
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

    // 관리자 가입 승인
    @Transactional
    public void approveAdmin(Long adminId){
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 관리자입니다.")
        );

        if (admin.getStatus() != AdminStatus.PENDING){
            throw new IllegalStateException("승인대기 상태인 관리자만 승인할 수 있습니다.");
        }

        admin.activate(LocalDateTime.now());
    }

    // 관리자 가입 거부
    @Transactional
    public void rejectAdmin(Long adminId, AdminRejectReasonRequest request){
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 관리자입니다.")
        );

        if (admin.getStatus() != AdminStatus.PENDING){
            throw new IllegalStateException("승인대기 상태인 관리자만 거부할 수 있습니다.");
        }

        admin.reject(request.getRejectReason(), LocalDateTime.now());
    }

    // 관리자 계정 삭제
    @Transactional
    public void deleteAdmin(Long adminId){
        Admin admin = adminRepository.findById(adminId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 관리자입니다.")
        );

        if (admin.getIsDeleted()){
            throw new IllegalStateException("이미 삭제된 관리자입니다.");
        }

        admin.deleteAdmin();
    }

}
