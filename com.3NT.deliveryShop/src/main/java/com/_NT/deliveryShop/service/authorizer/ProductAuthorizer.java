package com._NT.deliveryShop.service.authorizer;

import com._NT.deliveryShop.repository.helper.RepositoryHelper;
import com._NT.deliveryShop.repository.helper.ServiceErrorHelper;
import org.springframework.stereotype.Component;

@Component
public class ProductAuthorizer extends RestaurantAuthorizer {

    public ProductAuthorizer(
        AuthenticationInspector authInspector,
        ServiceErrorHelper errorHelper,
        RepositoryHelper repositoryHelper) {
        super(authInspector, errorHelper, repositoryHelper);
    }

}
