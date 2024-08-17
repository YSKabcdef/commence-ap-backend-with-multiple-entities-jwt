package portfolio.ecommencesite.service.impl;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import portfolio.ecommencesite.dto.CategoryDto;
import portfolio.ecommencesite.exception.ApiException;
import portfolio.ecommencesite.exception.ResourceNotFoundException;
import portfolio.ecommencesite.model.Category;
import portfolio.ecommencesite.repo.CategoryRepo;
import portfolio.ecommencesite.response.CategoryResponse;
import portfolio.ecommencesite.service.CategoryService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final ModelMapper modelMapper = new ModelMapper();
    private CategoryRepo categoryRepo;
    @Override
    public CategoryDto createCategory(CategoryDto category) {
        Optional<Category> check = categoryRepo.findByCategoryName(category.getCategoryName());
        if(check.isPresent()){
            throw new ApiException("Category with the name " + category.getCategoryName()+ " already exist!!" );
        }
        Category savingCategory = modelMapper.map(category,Category.class);
        Category savedCategory =  categoryRepo.save(savingCategory);
        return modelMapper.map(savedCategory,CategoryDto.class);
    }

    @Override
    public CategoryResponse findAllCategory(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Category> categoryPage = categoryRepo.findAll(pageDetails);
        List<CategoryDto> categories = categoryPage.getContent().stream().map(
                category -> modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());
        if(categories.isEmpty()){
            throw new ApiException("No category created till now");
        }
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categories);
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setTotalElement(categoryPage.getTotalElements());
        categoryResponse.setTotalPage(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());
        return categoryResponse;
    }

    @Override
    public CategoryDto deleteCategory(Long categoryId) {
        Category category = categoryRepo
                .findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category","category",categoryId));
        categoryRepo.delete(category);
        return  modelMapper.map(category,CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId) {
        Category category = categoryRepo
                .findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category","category",categoryId));
        category.setCategoryName(categoryDto.getCategoryName());
        categoryRepo.save(category);

        return modelMapper.map(category,CategoryDto.class);
    }
}
