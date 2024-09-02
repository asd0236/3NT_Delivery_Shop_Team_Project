package com._NT.deliveryShop.service.authorizer;

import com._NT.deliveryShop.domain.entity.Restaurant;
import com._NT.deliveryShop.domain.entity.User;
import com._NT.deliveryShop.repository.helper.RepositoryHelper;
import com._NT.deliveryShop.repository.helper.ServiceErrorHelper;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class RestaurantAuthorizer extends AbstractAuthorizer {

    public RestaurantAuthorizer(
        AuthenticationInspector authInspector,
        ServiceErrorHelper errorHelper,
        RepositoryHelper repositoryHelper) {
        super(authInspector, errorHelper, repositoryHelper);
    }

    public void requireRestaurantOwner(Authentication authentication,
        Restaurant requestedRestaurant) {
        if (authInspector.isAdmin(authentication))
            return;
        User user = authInspector.getUserOrThrow(authentication);

        if (user != requestedRestaurant.getOwner()) {
            throw errorHelper.forbidden("Not the owner of the restaurant");
        }
    }

    public void requireRestaurantOwner(Authentication authentication, UUID restaurantId) {
        Restaurant restaurant = repositoryHelper.findRestaurantOrThrow404(restaurantId);
        requireRestaurantOwner(authentication, restaurant);
    }
}
