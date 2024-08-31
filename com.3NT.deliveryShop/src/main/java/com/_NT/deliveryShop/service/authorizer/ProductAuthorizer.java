package com._NT.deliveryShop.service.authorizer;

import com._NT.deliveryShop.domain.entity.Product;
import com._NT.deliveryShop.repository.helper.RepositoryHelper;
import com._NT.deliveryShop.repository.helper.ServiceErrorHelper;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class ProductAuthorizer extends RestaurantAuthorizer {

    public ProductAuthorizer(
        AuthenticationInspector authInspector,
        ServiceErrorHelper errorHelper,
        RepositoryHelper repositoryHelper) {
        super(authInspector, errorHelper, repositoryHelper);
    }

    public void requireProductOwner(Authentication authentication, UUID productId) {
        Product product = repositoryHelper.findProductOrThrow404(productId);
        requireRestaurantOwner(authentication, product.getRestaurant());
    }
}
