package com._NT.deliveryShop.controller;


import com._NT.deliveryShop.domain.entity.UserRoleEnum;
import com._NT.deliveryShop.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    @GetMapping
    public ResponseEntity<String> auth(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        if(!userDetails.getUser().getRole().equals(UserRoleEnum.ADMIN))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");

        return ResponseEntity.ok("인증 성공");

    }
}
