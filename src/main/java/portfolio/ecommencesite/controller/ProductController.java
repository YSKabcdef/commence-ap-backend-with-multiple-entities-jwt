package portfolio.ecommencesite.controller;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import portfolio.ecommencesite.config.APIConstants;
import portfolio.ecommencesite.dto.ProductDto;
import portfolio.ecommencesite.response.ProductResponse;
import portfolio.ecommencesite.service.ProductService;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductService productService;
    @PostMapping("/admin/categories/{id}/product")
    public ResponseEntity<ProductDto> saveProductToCategory(
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody ProductDto productDto){
        return new ResponseEntity<>(productService.saveToCategory(id,productDto), HttpStatus.CREATED);
    }
    @GetMapping("/public/products")
    public  ResponseEntity<ProductResponse> getAllProduct(
            @RequestParam(name="pageNumber",defaultValue = APIConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = APIConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name="sortBy",defaultValue = APIConstants.SORT_PRODUCT_BY) String sortBy,
            @RequestParam(name="sortOrder",defaultValue = APIConstants.SORT_PRODUCT_DIR) String sortOrder
    ){
        return ResponseEntity.ok(productService.getAllProducts(pageNumber,pageSize,sortBy,sortOrder));

    }

    @GetMapping("/public/categories/{id}/product")
    public ResponseEntity<ProductResponse> getProductsById(@PathVariable("id")  Long productId,
                                                           @RequestParam(name="pageNumber",defaultValue = APIConstants.PAGE_NUMBER) Integer pageNumber,
                                                           @RequestParam(name = "pageSize",defaultValue = APIConstants.PAGE_SIZE) Integer pageSize,
                                                           @RequestParam(name="sortBy",defaultValue = APIConstants.SORT_PRODUCT_BY) String sortBy,
                                                           @RequestParam(name="sortOrder",defaultValue = APIConstants.SORT_PRODUCT_DIR) String sortOrder){
        return ResponseEntity.ok(productService.getProductsByCategory(productId,pageNumber,pageSize,sortBy,sortOrder));

    }
    @GetMapping("/public/products/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable("keyword")  String keyword,
                                                           @RequestParam(name="pageNumber",defaultValue = APIConstants.PAGE_NUMBER) Integer pageNumber,
                                                           @RequestParam(name = "pageSize",defaultValue = APIConstants.PAGE_SIZE) Integer pageSize,
                                                           @RequestParam(name="sortBy",defaultValue = APIConstants.SORT_PRODUCT_BY) String sortBy,
                                                           @RequestParam(name="sortOrder",defaultValue = APIConstants.SORT_PRODUCT_DIR) String sortOrder){
        return new ResponseEntity<>(productService.getProductsByKeyword(keyword,pageNumber,pageSize,sortBy,sortOrder),HttpStatus.FOUND);

    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@Valid  @RequestBody ProductDto productDto, @PathVariable("productId") Long productId){
        return new ResponseEntity<>(productService.updateProduct(productId,productDto),HttpStatus.OK);
    }
    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDto> deleteProduct(@PathVariable("productId") Long productId){
        return new ResponseEntity<>(productService.deleteProduct(productId),HttpStatus.OK);
    }
    @PutMapping("/public/products/{productId}/image")
    public ResponseEntity<ProductDto> updateProductImage(@PathVariable("productId") Long productId,
                                                         @RequestParam("image") MultipartFile image ) throws IOException {
        return new ResponseEntity<>(productService.updateProductImage(productId, image),HttpStatus.OK);
    }
}
