package com.commerceapp.admin.controller;

import com.commerceapp.admin.dto.*;
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

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody AdminSignupRequest request){

        adminService.signup(request);

        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 신청이 완료되었습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody AdminLoginRequest request, HttpServletRequest httprquest){
        AdminLoginSession loginSession = adminService.login(request);

        HttpSession session = httprquest.getSession(true);
        session.setAttribute("LoginAdmin", loginSession);
        session.setMaxInactiveInterval(864000);

        return ResponseEntity.status(HttpStatus.OK).body("로그인 성공!");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {

        HttpSession session = request.getSession(false);


        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.ok("성공적으로 로그아웃 되었습니다.");
    }

    @GetMapping("/{adminId}")
    public ResponseEntity<AdminDetailResponse> getAdminDetail(@PathVariable Long adminId){

        AdminDetailResponse response = adminService.getAdminDetail(adminId);

        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<AdminProfileResponse> getMyProfile(
            @SessionAttribute(name = "LoginAdmin")
            AdminLoginSession loginSession){
        AdminProfileResponse response = adminService.getMyProfile(loginSession.getId());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{adminId}")
    public ResponseEntity<String> updateAdmin(
            @PathVariable Long adminId,
            @Valid @RequestBody AdminUpdateRequest request){

        adminService.adminUpdate(adminId, request);

        return ResponseEntity.status(HttpStatus.OK).body("관리자 정보 수정이 완료되었습니다.");
    }

    @PatchMapping("/me")
    public ResponseEntity<String> updateMyProfile(
            @Valid @RequestBody AdminMyProfileUpdateRequest request,
            @SessionAttribute(name = "LoginAdmin")
            AdminLoginSession loginSession){

        adminService.updateMyProfile(loginSession.getId(), request);

        return ResponseEntity.status(HttpStatus.OK).body("프로필 정보가 수정되었습니다.");
    }

    @PatchMapping("/changerole/{adminId}")
    public ResponseEntity<String> changeAdminRole(
            @PathVariable Long adminId,
            @Valid @RequestBody AdminRoleUpdateRequest request){

        adminService.changeAdminRole(adminId, request);

         return ResponseEntity.status(HttpStatus.OK).body("관리자 역할 변경이 완료되었습니다.");
    }

    @PatchMapping("/changestatus/{adminId}")
    public ResponseEntity<String> changeAdminStatus(
            @PathVariable Long adminId,
            @Valid @RequestBody AdminStatusUpdateRequest request){

        adminService.changeAdminStatus(adminId, request);

        return ResponseEntity.status(HttpStatus.OK).body("관리자 상태 변경이 완료되었습니다.");
    }

    @PatchMapping("/approve/{adminId}")
    public ResponseEntity<String> approveAdmin(@PathVariable Long adminId) {

        adminService.approveAdmin(adminId);

        return ResponseEntity.status(HttpStatus.OK).body("관리자 가입이 승인되었습니다.");

    }

    @PatchMapping("/reject/{adminId}")
    public ResponseEntity<String> rejectAdmin(
            @PathVariable Long adminId,
            @Valid @RequestBody AdminRejectReasonRequest request){

        adminService.rejectAdmin(adminId, request);

        return ResponseEntity.status(HttpStatus.OK).body("관리자 가입이 거부되었습니다.");

    }

    @DeleteMapping("/delete/{adminId}")
    public ResponseEntity<String> deleteAdmin(@PathVariable Long adminId){

        adminService.deleteAdmin(adminId);

        return ResponseEntity.status(HttpStatus.OK).body("계정이 삭제되었습니다.");
    }

}
