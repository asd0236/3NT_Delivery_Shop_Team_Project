package com._NT.deliveryShop.service.authorizer;


import com._NT.deliveryShop.domain.entity.User;
import com._NT.deliveryShop.repository.helper.ServiceErrorHelper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;

@AllArgsConstructor
public class AbstractAuthorizer {

    protected final AuthenticationInspector authInspector;
    protected final ServiceErrorHelper errorHelper;

    public void requireByOneself(Authentication authentication, User requester) {
        if (authInspector.isAdmin(authentication))
            return;
        User user = authInspector.getUserOrThrow(authentication);

        if (user != requester) {
            throw errorHelper.forbidden("Not requested oneself");
        }
    }
}
