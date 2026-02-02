package in.maisonnoir.backend.api.product.controller;

import  in.maisonnoir.backend.api.common.response.ApiResponse;
import in.maisonnoir.backend.api.product.model.dto.ProductRequestDTO;
import in.maisonnoir.backend.api.product.model.dto.ProductResponseDTO;
import in.maisonnoir.backend.api.product.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO) {
        ProductResponseDTO product = productService.createProduct(productRequestDTO);
        return ResponseEntity.ok(
                new ApiResponse(true, "Product created successfully", product)
        );
    }

    @PutMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateProduct(
            @PathVariable String productId,
            @Valid @RequestBody ProductRequestDTO productRequestDTO) {

        ProductResponseDTO product = productService.updateProduct(productId, productRequestDTO);
        return ResponseEntity.ok(
                new ApiResponse(true, "Product updated successfully", product)
        );
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable String productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(
                new ApiResponse(true, "Product deleted successfully", null)
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllProducts() {
        List<ProductResponseDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(
                new ApiResponse(true, "Products fetched successfully", products)
        );
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable String productId) {
        ProductResponseDTO product = productService.getProductById(productId);
        return ResponseEntity.ok(
                new ApiResponse(true, "Product fetched successfully", product)
        );
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse> getProductsByCategory(@PathVariable String category) {
        List<ProductResponseDTO> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(
                new ApiResponse(true, "Products fetched by category successfully", products)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchProductsByName(@RequestParam String name) {
        List<ProductResponseDTO> products = productService.searchProductsByName(name);
        return ResponseEntity.ok(
                new ApiResponse(true, "Products searched successfully", products)
        );
    }

}
