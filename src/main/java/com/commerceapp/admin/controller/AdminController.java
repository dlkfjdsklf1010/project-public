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

    @GetMapping("/{adminId}")
    public ResponseEntity<AdminDetailResponse> getAdminDetail(@PathVariable Long adminId){

        AdminDetailResponse response = adminService.getAdminDetail(adminId);

        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{adminId}")
    public ResponseEntity<String> updateAdmin(
            @PathVariable Long adminId,
            @Valid @RequestBody AdminUpdateRequest request){

        adminService.adminUpdate(adminId, request);

        return ResponseEntity.status(HttpStatus.OK).body("관리자 정보 수정이 완료되었습니다.");
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

}
