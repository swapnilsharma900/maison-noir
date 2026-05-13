package in.maisonnoir.backend.api.product.service;

import in.maisonnoir.backend.api.product.model.dto.ProductRequestDTO;
import in.maisonnoir.backend.api.product.model.dto.ProductResponseDTO;

import java.util.List;

public interface ProductService {

    // ADMIN SERVICES
    ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO);

    ProductResponseDTO updateProduct(String productId, ProductRequestDTO productRequestDTO);

    void deleteProduct(String productId);

    // CUSTOMER SERVICES
    ProductResponseDTO getProductById(String productId);

    List<ProductResponseDTO> getAllProducts();

    List<ProductResponseDTO> getProductsByCategory(String category);

    List<ProductResponseDTO> searchProductsByName(String name);

}
