package com.commerceapp.admin.controller;

import com.commerceapp.admin.dto.AdminSignupRequest;
import com.commerceapp.admin.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
