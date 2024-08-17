package portfolio.ecommencesite.service;

import org.springframework.web.multipart.MultipartFile;
import portfolio.ecommencesite.dto.ProductDto;
import portfolio.ecommencesite.response.ProductResponse;

import java.io.IOException;

public interface ProductService {
    ProductDto saveToCategory(Long categoryId, ProductDto productDto);
    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ProductResponse getProductsByCategory(Long id, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ProductResponse getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ProductDto updateProduct(Long productId, ProductDto productDto);
    ProductDto deleteProduct(Long productId);

    ProductDto updateProductImage(Long productId, MultipartFile image) throws IOException;
}
