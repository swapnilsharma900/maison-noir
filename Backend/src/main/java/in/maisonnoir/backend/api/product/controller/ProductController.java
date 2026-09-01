package in.maisonnoir.backend.api.product.controller;

import  in.maisonnoir.backend.api.common.response.ApiResponse;
import in.maisonnoir.backend.api.product.model.dto.ProductRequestDTO;
import in.maisonnoir.backend.api.product.model.dto.ProductResponseDTO;
import in.maisonnoir.backend.api.product.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Endpoints for browsing and managing products (catalog)")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a product (Admin)", description = "Creates a new product in the catalog with variants and pricing. Admin access only.")
    public ResponseEntity<ApiResponse> createProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO) {
        ProductResponseDTO product = productService.createProduct(productRequestDTO);
        return ResponseEntity.ok(
                new ApiResponse(true, "Product created successfully", product)
        );
    }

    @PutMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a product (Admin)", description = "Updates an existing product's details, variants, and pricing. Admin access only.")
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
    @Operation(summary = "Delete a product (Admin)", description = "Permanently removes a product from the catalog. Admin access only.")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable String productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(
                new ApiResponse(true, "Product deleted successfully", null)
        );
    }

    @GetMapping
    @Operation(summary = "Get all products", description = "Fetches the complete product catalog. Public access — no authentication required.")
    public ResponseEntity<ApiResponse> getAllProducts() {
        List<ProductResponseDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(
                new ApiResponse(true, "Products fetched successfully", products)
        );
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Get product by ID", description = "Fetches a single product's full details including variants. Public access.")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable String productId) {
        ProductResponseDTO product = productService.getProductById(productId);
        return ResponseEntity.ok(
                new ApiResponse(true, "Product fetched successfully", product)
        );
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get products by category", description = "Filters products by category name. Public access.")
    public ResponseEntity<ApiResponse> getProductsByCategory(@PathVariable String category) {
        List<ProductResponseDTO> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(
                new ApiResponse(true, "Products fetched by category successfully", products)
        );
    }

    @GetMapping("/search")
    @Operation(summary = "Search products by name", description = "Searches products by name using partial matching. Public access.")
    public ResponseEntity<ApiResponse> searchProductsByName(@RequestParam String name) {
        List<ProductResponseDTO> products = productService.searchProductsByName(name);
        return ResponseEntity.ok(
                new ApiResponse(true, "Products searched successfully", products)
        );
    }

}
