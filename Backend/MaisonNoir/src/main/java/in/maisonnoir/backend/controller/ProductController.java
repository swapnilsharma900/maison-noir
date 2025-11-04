package in.maisonnoir.backend.controller;

import in.maisonnoir.backend.model.Product;
import in.maisonnoir.backend.payload.ApiResponse;
import in.maisonnoir.backend.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createProduct(@Valid @RequestBody Product product) {
        productService.createProduct(product);
        return ResponseEntity.ok(new ApiResponse("Product created successfully"));
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new ApiResponse("Product Deleted successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody Product product) {
        productService.updateProduct(id, product);
        return ResponseEntity.ok(new ApiResponse("Product updated successfully"));
    }


    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> patchProduct(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        productService.updateFields(id, updates);
        return ResponseEntity.ok(new ApiResponse("Field(s) updated successfully"));
    }


}
