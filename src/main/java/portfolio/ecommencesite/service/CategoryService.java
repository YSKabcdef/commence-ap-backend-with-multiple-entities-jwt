package portfolio.ecommencesite.service;

import portfolio.ecommencesite.dto.CategoryDto;
import portfolio.ecommencesite.response.CategoryResponse;


public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);
    CategoryResponse findAllCategory(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    CategoryDto deleteCategory(Long categoryId);
    CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId);
}
