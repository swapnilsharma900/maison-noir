package in.maisonnoir.backend.api.product.service;

import in.maisonnoir.backend.api.product.model.dto.ProductDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductService {

    ProductDTO createProduct(ProductDTO dto);

    List<ProductDTO> getAllProducts();

    Optional<ProductDTO> getProductById(String id);

    ProductDTO updateProduct(String id, ProductDTO updatedDTO);

    ProductDTO updateFields(String id, Map<String, Object> updatedFields);

    void deleteProduct(String id);
}
