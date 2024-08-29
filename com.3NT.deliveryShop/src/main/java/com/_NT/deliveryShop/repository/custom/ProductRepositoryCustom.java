package com._NT.deliveryShop.repository.custom;

import com._NT.deliveryShop.domain.entity.Product;
import com._NT.deliveryShop.repository.searchcondition.ProductSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

    Page<Product> search(ProductSearchCondition productSearchCondition, Pageable pageable);
}

