package portfolio.ecommencesite.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import portfolio.ecommencesite.dto.CartDto;
import portfolio.ecommencesite.dto.ProductDto;
import portfolio.ecommencesite.exception.ApiException;
import portfolio.ecommencesite.exception.ResourceNotFoundException;
import portfolio.ecommencesite.model.Cart;
import portfolio.ecommencesite.model.Category;
import portfolio.ecommencesite.model.Product;
import portfolio.ecommencesite.repo.CartRepo;
import portfolio.ecommencesite.repo.CategoryRepo;
import portfolio.ecommencesite.repo.ProductRepo;
import portfolio.ecommencesite.response.ProductResponse;
import portfolio.ecommencesite.service.CartService;
import portfolio.ecommencesite.service.FileService;
import portfolio.ecommencesite.service.ProductService;

import java.io.File;
import java.io.IOException;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepo cartRepo;

    @Override
    public ProductDto saveToCategory(Long categoryId, ProductDto productDto) {
        if (productRepo.findByProductName(productDto.getProductName()).isPresent()){
            throw new ApiException("Product Name:" + productDto.getProductName() +" exist");
        }

        Category category = categoryRepo
                .findById(categoryId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Category", "category", categoryId)
                );
        Product product = modelMapper.map(productDto, Product.class);
        product.setImage("default.png");
        product.setSpecialPrice(productDto.getPrice() * (1 - productDto.getDiscount() * 0.01));
        product.setCategory(category);
        return modelMapper.map(productRepo.save(product), ProductDto.class);
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPage = productRepo.findAll(pageDetails);
        List<ProductDto> products = productPage.getContent()
                .stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();
        if (products.isEmpty()){
            throw new ApiException("That is no products created.");
        }
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(products);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setTotalElement(productPage.getTotalElements());
        productResponse.setLastPage(productPage.isLast());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalPage(productPage.getTotalPages());
        return productResponse;
    }

    @Override
    public ProductResponse getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Category category = categoryRepo
                .findById(categoryId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Category", "category", categoryId)
                );
        Page<Product> productPage = productRepo.findByCategory(category, pageDetails);
        ProductResponse productResponse = new ProductResponse();
        List<ProductDto> products = productPage.getContent()
                .stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();
        if (products.isEmpty()){
            throw new ApiException("That is no products created under this category");
        }
        productResponse.setContent(products);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setTotalElement(productPage.getTotalElements());
        productResponse.setLastPage(productPage.isLast());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalPage(productPage.getTotalPages());
        return productResponse;
    }

    @Override
    public ProductResponse getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPage = productRepo.findAllByProductNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword, pageDetails);
        List<ProductDto> products = productPage.getContent()
                .stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();
        if (products.isEmpty()){
            throw new ApiException("That is no products with this keyword");
        }
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(products);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setTotalElement(productPage.getTotalElements());
        productResponse.setLastPage(productPage.isLast());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalPage(productPage.getTotalPages());
        return productResponse;
    }

    @Override
    public ProductDto updateProduct(Long productId, ProductDto productDto) {
        Product product = productRepo.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "product", productId)
        );
        product.setProductName(productDto.getProductName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setDiscount(productDto.getDiscount());
        product.setSpecialPrice(productDto.getPrice() * (100 - productDto.getDiscount()) * 0.01);
        product.setQuantity(productDto.getQuantity());
        ProductDto savedProductDto = modelMapper.map(productRepo.save(product), ProductDto.class);
        List<Cart> carts = cartRepo.findCartsByProductId(productId);
        List<CartDto> cartDtos = carts.stream().map(cart -> {
            CartDto cartDto = modelMapper.map(cart,CartDto.class);
            List<ProductDto> productDtos = cart.getCartItems().stream()
                    .map(
                            item -> {
                               ProductDto innerProductDto = modelMapper.map(item,ProductDto.class);
                                innerProductDto.setQuantity(item.getQuantity());
                                return innerProductDto;
                            }
                    ).toList();
            cartDto.setCartItems(productDtos);
            return cartDto;
        }).toList();
        cartDtos.forEach(cartDto -> cartService.updateProductInCart(productId,cartDto.getCartId()));
        return savedProductDto;
    }

    @Override
    public ProductDto deleteProduct(Long productId) {
        Product product = productRepo.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "product", productId)
        );
        productRepo.delete(product);
        List<Cart> carts = cartRepo.findCartsByProductId(productId);
        carts.forEach(cart -> cartService.deleteProductFromCart(cart.getCartId(),productId));
        return modelMapper.map(product, ProductDto.class);

    }

    @Override
    public ProductDto updateProductImage(Long productId, MultipartFile image) throws IOException {
        // product from db
        Product product = productRepo.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "product", productId)
        );
        //upload image to server
        //get file name of uploaded image
        File file = new File(path+File.separator+product.getImage());
        if(file.exists()){
            file.delete();
        }
        String fileName = fileService.uploadImage(path, image);
        //Updating on the new file name on the product
        product.setImage(fileName);
        //return DTO after mapping to DTO
        return modelMapper.map(productRepo.save(product), ProductDto.class);
    }


}