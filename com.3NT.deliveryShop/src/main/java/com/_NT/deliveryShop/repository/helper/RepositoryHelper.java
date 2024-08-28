package com._NT.deliveryShop.repository.helper;

import com._NT.deliveryShop.domain.entity.Category;
import com._NT.deliveryShop.domain.entity.Restaurant;
import com._NT.deliveryShop.domain.entity.User;
import jakarta.persistence.EntityManager;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public record RepositoryHelper(EntityManager em, ServiceErrorHelper errorHelper) {

    public <T> T findOrThrow404(Class<T> clazz, Object primaryKey) throws ResponseStatusException {
        T entity = em.find(clazz, primaryKey);
        if (entity == null) {
            String reason = String.format("%s id was not found", clazz.getName());
            throw errorHelper.notFound(reason);
        }
        return entity;
    }

    public Category findCategoryOrThrow404(UUID id) throws ResponseStatusException {
        return findOrThrow404(Category.class, id);
    }

    public Restaurant findRestaurantOrThrow404(UUID id) throws ResponseStatusException {
        return findOrThrow404(Restaurant.class, id);
    }

    public User findUserOrThrow404(Long id) throws ResponseStatusException {
        return findOrThrow404(User.class, id);
    }

    }
}
