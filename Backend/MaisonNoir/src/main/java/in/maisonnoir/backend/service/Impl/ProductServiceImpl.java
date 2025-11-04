package in.maisonnoir.backend.service.Impl;

import in.maisonnoir.backend.Repository.ProductRepository;
import in.maisonnoir.backend.model.Product;
import in.maisonnoir.backend.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product updateProduct(Long id, Product updatedProduct) {
        return productRepository.findById(id)
                .map(existing -> {
                    existing.setName(updatedProduct.getName());
                    existing.setDescription(updatedProduct.getDescription());
                    existing.setPrice(updatedProduct.getPrice());
                    existing.setCategory(updatedProduct.getCategory());
                    existing.setImage(updatedProduct.getImage());
                    existing.setIsAvailable(updatedProduct.getIsAvailable());
                    return productRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public Product updateFields(Long id, Map<String, Object> updatedProduct) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        updatedProduct.forEach((key, value) -> {
            switch (key) {
                case "price" -> product.setPrice(Double.valueOf(value.toString()));
                case "isAvailable" -> product.setIsAvailable(Boolean.valueOf(value.toString()));
                case "name" -> product.setName(value.toString());
                case "description" -> product.setDescription(value.toString());
                case "category" -> product.setCategory(value.toString());
                case "image" -> product.setImage(value.toString());
            }
        });

        return productRepository.save(product);

    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

}
