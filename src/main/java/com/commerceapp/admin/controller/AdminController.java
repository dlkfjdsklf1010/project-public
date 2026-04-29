package com.commerceapp.admin.controller;

import com.commerceapp.admin.dto.*;
import com.commerceapp.admin.enums.AdminRole;
import com.commerceapp.admin.enums.AdminStatus;
import com.commerceapp.admin.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admins")
public class AdminController {

    private final AdminService adminService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> adminSignup(@Valid @RequestBody AdminSignupRequest request){
        adminService.adminSignup(request);

        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 신청이 완료되었습니다.");
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<String> adminLogin(@Valid @RequestBody AdminLoginRequest request, HttpServletRequest requestHttp){
        AdminLoginSession loginSession = adminService.adminLogin(request);

        HttpSession session = requestHttp.getSession(true);
        session.setAttribute("loginAdmin", loginSession);
        session.setMaxInactiveInterval(864000);

        return ResponseEntity.status(HttpStatus.OK).body("관리자 로그인 성공!");
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<String> adminLogout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.ok("성공적으로 로그아웃 되었습니다.");
    }

    // 슈퍼 관리자 권한인지 확인
    private void validAdmin(AdminLoginSession loginSession){
        if (loginSession == null){
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        if (!AdminRole.SUPER.getDisplayName().equals(loginSession.getRole())){
            throw new IllegalStateException("권한이 없습니다.");
        }
    }

    // 관리자 리스트 조회
    @GetMapping
    public ResponseEntity<AdminPageResponse> getAdminList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) AdminRole role,
            @RequestParam(required = false) AdminStatus status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @SessionAttribute(name = "loginAdmin", required = false)
            AdminLoginSession loginSession){
        // 슈퍼관리자 권한 검증
        validAdmin(loginSession);

        AdminPageResponse response = adminService.getAdminList(
                keyword, role, status, page, size, sortBy, direction);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 관리자 상세 조회
    @GetMapping("/{adminId}")
    public ResponseEntity<AdminDetailResponse> getAdminDetail(
            @PathVariable Long adminId,
            @SessionAttribute(name = "loginAdmin", required = false)
            AdminLoginSession loginSession){
        // 슈퍼관리자 권한 검증
        validAdmin(loginSession);

        AdminDetailResponse response = adminService.getAdminDetail(adminId);

        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 관리자 내 프로필 조회
    @GetMapping("/me")
    public ResponseEntity<AdminProfileResponse> getMyProfile(
            @SessionAttribute(name = "loginAdmin")
            AdminLoginSession loginSession){
        AdminProfileResponse response = adminService.getMyProfile(loginSession.getId());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 관리자 정보 수정
    @PatchMapping("/{adminId}")
    public ResponseEntity<String> updateAdmin(
            @PathVariable Long adminId,
            @Valid @RequestBody AdminUpdateRequest request,
            @SessionAttribute(name = "loginAdmin", required = false)
            AdminLoginSession loginSession){

        // 슈퍼관리자 권한 검증
        validAdmin(loginSession);

        adminService.adminUpdate(adminId, request);

        return ResponseEntity.status(HttpStatus.OK).body("관리자 정보 수정이 완료되었습니다.");
    }

    // 관리자 내 프로필 수정
    @PatchMapping("/me")
    public ResponseEntity<String> updateMyProfile(
            @Valid @RequestBody AdminMyProfileUpdateRequest request,
            @SessionAttribute(name = "loginAdmin")
            AdminLoginSession loginSession){

        adminService.updateMyProfile(loginSession.getId(), request);

        return ResponseEntity.status(HttpStatus.OK).body("프로필 정보가 수정되었습니다.");
    }

    // 관리자 내 비밀번호 수정
    @PatchMapping("/mypassword")
    public ResponseEntity<String> updateMyPassword(
            @Valid @RequestBody AdminMyPasswordUpdateRequest request,
            @SessionAttribute(name = "loginAdmin")
            AdminLoginSession loginSession){

        adminService.updateMyPassword(loginSession.getId(), request);

        return ResponseEntity.status(HttpStatus.OK).body("비밀번호가 수정되었습니다.");
    }

    // 관리자 역할 변경
    @PatchMapping("/changerole/{adminId}")
    public ResponseEntity<String> changeAdminRole(
            @PathVariable Long adminId,
            @Valid @RequestBody AdminRoleUpdateRequest request,
            @SessionAttribute(name = "loginAdmin", required = false)
            AdminLoginSession loginSession){

        // 슈퍼관리자 권한 검증
        validAdmin(loginSession);

        adminService.changeAdminRole(adminId, request);

         return ResponseEntity.status(HttpStatus.OK).body("관리자 역할 변경이 완료되었습니다.");
    }

    // 관리자 상태 변경
    @PatchMapping("/changestatus/{adminId}")
    public ResponseEntity<String> changeAdminStatus(
            @PathVariable Long adminId,
            @Valid @RequestBody AdminStatusUpdateRequest request,
            @SessionAttribute(name = "loginAdmin", required = false)
            AdminLoginSession loginSession){

        // 슈퍼관리자 권한 검증
        validAdmin(loginSession);

        adminService.changeAdminStatus(adminId, request);

        return ResponseEntity.status(HttpStatus.OK).body("관리자 상태 변경이 완료되었습니다.");
    }

    // 관리자 가입 승인
    @PatchMapping("/approve/{adminId}")
    public ResponseEntity<String> approveAdmin(
            @PathVariable Long adminId,
            @SessionAttribute(name = "loginAdmin", required = false)
            AdminLoginSession loginSession) {

        // 슈퍼관리자 권한 검증
        validAdmin(loginSession);

        adminService.approveAdmin(adminId);

        return ResponseEntity.status(HttpStatus.OK).body("관리자 가입이 승인되었습니다.");
    }

    // 관리자 가입 거부
    @PatchMapping("/reject/{adminId}")
    public ResponseEntity<String> rejectAdmin(
            @PathVariable Long adminId,
            @Valid @RequestBody AdminRejectReasonRequest request,
            @SessionAttribute(name = "loginAdmin", required = false)
            AdminLoginSession loginSession) {

        // 슈퍼관리자 권한 검증
        validAdmin(loginSession);

        adminService.rejectAdmin(adminId, request);

        return ResponseEntity.status(HttpStatus.OK).body("관리자 가입이 거부되었습니다.");
    }

    // 관리자 계정 삭제
    @DeleteMapping("/delete/{adminId}")
    public ResponseEntity<String> deleteAdmin(
            @PathVariable Long adminId,
            @SessionAttribute(name = "loginAdmin", required = false)
            AdminLoginSession loginSession){

        // 슈퍼관리자 권한 검증
        validAdmin(loginSession);

        adminService.deleteAdmin(adminId);

        return ResponseEntity.status(HttpStatus.OK).body("계정이 삭제되었습니다.");
    }
}
