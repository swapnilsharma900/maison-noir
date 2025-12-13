package in.maisonnoir.backend.api.product.service.impl;

import in.maisonnoir.backend.api.product.model.dto.ProductDTO;
import in.maisonnoir.backend.api.product.repository.ProductDAO;
import in.maisonnoir.backend.api.product.mapper.ProductMapper;
import in.maisonnoir.backend.api.product.model.entity.ProductEntity;
import in.maisonnoir.backend.api.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductDAO productDAO;

    @Override
    public ProductDTO createProduct(ProductDTO dto) {
        ProductEntity entity = ProductMapper.toEntity(dto);
        ProductEntity saved =  productDAO.save(entity);
        return ProductMapper.toDTO(saved);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productDAO.findAll()
                .stream()
                .map(ProductMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductDTO> getProductById(String id) {
        return productDAO.findById(id)
                .map(ProductMapper::toDTO);

    }

    @Override
    public ProductDTO updateProduct(String id, ProductDTO updatedDTO) {
        ProductEntity existing = productDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existing.setName(updatedDTO.getName());
        existing.setDescription(updatedDTO.getDescription());
        existing.setPrice(updatedDTO.getPrice());
        existing.setCategory(updatedDTO.getCategory());
        existing.setImage(updatedDTO.getImage());
        existing.setIsAvailable(updatedDTO.getIsAvailable());

        ProductEntity saved = productDAO.save(existing);
        return ProductMapper.toDTO(saved);

    }

    @Override
    public ProductDTO updateFields(String id, Map<String, Object> updatedFields) {
        ProductEntity existing = productDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        updatedFields.forEach((key, value) -> {
            switch (key) {
                case "name" -> existing.setName((String) value);
                case "description" -> existing.setDescription((String) value);
                case "price" -> existing.setPrice(Double.parseDouble(value.toString()));
                case "category" -> existing.setCategory((String) value);
                case "imageUrl" -> existing.setImage((String) value);
                case "isAvailable" -> existing.setIsAvailable(Boolean.parseBoolean(value.toString()));
                default -> throw new IllegalArgumentException("Invalid field: " + key);
            }
        });

        ProductEntity saved = productDAO.save(existing);
        return ProductMapper.toDTO(saved);
    }

    @Override
    public void deleteProduct(String id) {
        if (!productDAO.existsById(id)) {
            throw new RuntimeException("Product not found");
        }
        productDAO.deleteById(id);

    }

}
