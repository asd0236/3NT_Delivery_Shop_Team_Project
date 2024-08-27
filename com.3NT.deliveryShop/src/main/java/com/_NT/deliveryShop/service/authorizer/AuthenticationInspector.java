package com._NT.deliveryShop.service.authorizer;

import com._NT.deliveryShop.domain.entity.User;
import com._NT.deliveryShop.repository.UserRepository;
import com._NT.deliveryShop.repository.helper.ServiceErrorHelper;
import com._NT.deliveryShop.security.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public record AuthenticationInspector(
    UserRepository userRepository,
    ServiceErrorHelper errorHelper) {

    public User getUserOrThrow(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw errorHelper.unauthorized("Not authenticated");
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        return userRepository.findById(user.getUserId())
            .orElseThrow(() -> errorHelper.forbidden("Removed user"));
    }

    public boolean isAdmin(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw errorHelper.unauthorized("Not authenticated");
        }
        return authentication.getAuthorities().stream()
            .anyMatch(authority -> authority.getAuthority().equals("ADMIN"));
    }

}
