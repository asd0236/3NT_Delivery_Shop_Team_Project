package com._NT.deliveryShop.util;

import com._NT.deliveryShop.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * timestamped entity에 대한 생성자나 수정자를 설정하기 위한 AuditorAware 구현체
 * SecurityContextHoder에서 현재 사용자를 가져와서 반환합니다.
 * 로그인한 사용자가 없을 경우 Optional.empty() 를 반환합니다.
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class UserAuditorAware implements AuditorAware<Long> {
    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return Optional.of(Long.valueOf(userDetails.getUsername()));
        }
        return Optional.empty();
    }

}
