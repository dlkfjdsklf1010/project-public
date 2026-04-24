package com.commerceapp.admin.controller;

import com.commerceapp.admin.dto.AdminDetailResponse;
import com.commerceapp.admin.dto.AdminSignupRequest;
import com.commerceapp.admin.dto.AdminUpdateRequest;
import com.commerceapp.admin.service.AdminService;
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

    @GetMapping("/{adminId}")
    public ResponseEntity<AdminDetailResponse> getAdminDetail(@PathVariable Long adminId){

        AdminDetailResponse response = adminService.getAdminDetail(adminId);

        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{adminId}")
    public ResponseEntity<String> updateAdmin(@PathVariable Long adminId,@Valid @RequestBody AdminUpdateRequest request){

        adminService.adminUpdate(adminId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body("관리자 정보 수정이 완료되었습니다.");
    }
}
