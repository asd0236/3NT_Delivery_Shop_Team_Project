package com._NT.deliveryShop.service;

import static com._NT.deliveryShop.domain.dto.CategoryDto.Create;
import static com._NT.deliveryShop.domain.dto.CategoryDto.Put;
import static com._NT.deliveryShop.domain.dto.CategoryDto.Result;

import com._NT.deliveryShop.domain.entity.Category;
import com._NT.deliveryShop.repository.CategoryRepository;
import com._NT.deliveryShop.repository.helper.RepositoryHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final RepositoryHelper repoHelper;

    @Transactional
    public Result createCategory(Create dto) {
        Category category = dto.asEntity();

        return Result.of(categoryRepository.save(category));
    }

    public Result readCategory(Long id) {
        return Result.of(repoHelper.findCategoryOrThrow404(id));
    }

    @Transactional
    public Result putCategory(Long id, Put dto) {
        Category category = repoHelper.findCategoryOrThrow404(id);

        if (dto != null) {
            category.setName(dto.getName());
        }

        return Result.of(categoryRepository.save(category));
    }
}
