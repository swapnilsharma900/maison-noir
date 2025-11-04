package in.maisonnoir.backend.service;

import in.maisonnoir.backend.model.Product;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProducts();
    Product createProduct(Product product);
    Optional<Product> getProductById(Long id);
    Product updateProduct(Long id, Product updatedProduct);
    Product updateFields(Long id, Map<String, Object> updatedProduct);
    void deleteProduct(Long id);
}
