package in.maisonnoir.backend.service;

import in.maisonnoir.backend.DTO.ProductDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductService {

    ProductDTO createProduct(ProductDTO dto);

    List<ProductDTO> getAllProducts();

    Optional<ProductDTO> getProductById(Long id);

    ProductDTO updateProduct(Long id, ProductDTO updatedDTO);

    ProductDTO updateFields(Long id, Map<String, Object> updatedFields);

    void deleteProduct(Long id);
}
