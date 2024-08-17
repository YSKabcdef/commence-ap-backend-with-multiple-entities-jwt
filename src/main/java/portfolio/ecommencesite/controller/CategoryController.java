package portfolio.ecommencesite.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portfolio.ecommencesite.config.APIConstants;
import portfolio.ecommencesite.dto.CategoryDto;
import portfolio.ecommencesite.response.CategoryResponse;
import portfolio.ecommencesite.service.CategoryService;



@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class CategoryController {
    private CategoryService categoryService;
        @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> listCategories(
            @RequestParam(name="pageNumber" , defaultValue = APIConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name="pageSize", defaultValue = APIConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = APIConstants.SORT_CATEGORY_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = APIConstants.SORT_CATEGORY_DIR) String sortOrder
            ){
        CategoryResponse categories = categoryService.findAllCategory(pageNumber,pageSize,sortBy,sortOrder);
        return ResponseEntity.ok(categories);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<CategoryDto> addCategory(@Valid @RequestBody CategoryDto category){
        CategoryDto categoryDto  = categoryService.createCategory(category);
        return new ResponseEntity<>(categoryDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/public/categories/{id}")
    public ResponseEntity<CategoryDto> deleteCategory(@PathVariable("id") Long categoryId){
        CategoryDto categoryDto  = categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(categoryDto);
    }
    @PutMapping("/public/categories/{id}")
    public  ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto, @PathVariable("id") Long categoryId){
        CategoryDto category =  categoryService.updateCategory(categoryDto,categoryId);
        return  ResponseEntity.ok(category);
    }
}
