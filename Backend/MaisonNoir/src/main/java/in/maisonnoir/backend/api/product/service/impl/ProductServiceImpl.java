package in.maisonnoir.backend.api.product.service.impl;

import in.maisonnoir.backend.api.common.exception.DuplicateResourceException;
import in.maisonnoir.backend.api.common.exception.ResourceNotFoundException;
import in.maisonnoir.backend.api.product.model.dto.ProductRequestDTO;
import in.maisonnoir.backend.api.product.model.dto.ProductResponseDTO;
import in.maisonnoir.backend.api.product.repository.ProductDAO;
import in.maisonnoir.backend.api.product.mapper.ProductMapper;
import in.maisonnoir.backend.api.product.model.entity.ProductEntity;
import in.maisonnoir.backend.api.product.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductDAO productDAO;

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        // Check if product with same name already exists
        /* allow products with same name
        if (productDAO.existsByProductName(productRequestDTO.getName())) {
            throw new DuplicateResourceException(
                    "Product",
                    "name",
                    productRequestDTO.getName(),
                    "Product with this name already exists");
        }
        */

        ProductEntity entity = ProductMapper.toEntity(productRequestDTO);
        ProductEntity saved = productDAO.save(entity);

        log.info("Product created with itemId: {}", saved.getProductId());
        return ProductMapper.toResponse(saved);
    }

    @Override
    public ProductResponseDTO updateProduct(String productId, ProductRequestDTO productRequestDTO) {
        ProductEntity entity = productDAO.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        // Check if updating name to an existing product name
        if (productRequestDTO.getName() != null
                && !productRequestDTO.getName().equals(entity.getProductName())
                && productDAO.existsByProductName(productRequestDTO.getName())) {
            throw new DuplicateResourceException(
                    "Product",
                    "name",
                    productRequestDTO.getName(),
                    "Product with this name already exists");
        }

        ProductMapper.applyUpdate(productRequestDTO, entity);
        ProductEntity updated = productDAO.save(entity);

        log.info("Product updated with itemId: {}", productId);
        return ProductMapper.toResponse(updated);
    }

    @Override
    public void deleteProduct(String productId) {
        ProductEntity entity = productDAO.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        productDAO.deleteById(productId);

        log.info("Product deleted with itemId: {}", productId);
    }

    @Override
    public ProductResponseDTO getProductById(String productId) {
        ProductEntity entity = productDAO.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        log.info("Fetched product with itemId: {}", productId);
        return ProductMapper.toResponse(entity);
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        List<ProductEntity> products = productDAO.findAll();

        log.info("Fetched all products: {} items", products.size());
        return products.stream()
                .map(ProductMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDTO> getProductsByCategory(String category) {
        List<ProductEntity> products = productDAO.findByProductCategory(category);

        log.info("Fetched products by category '{}': {} items", category, products.size());
        return products.stream()
                .map(ProductMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDTO> searchProductsByName(String name) {
        List<ProductEntity> products = productDAO.findByProductNameContainingIgnoreCase(name);

        log.info("Searched products by name '{}': {} items found", name, products.size());
        return products.stream()
                .map(ProductMapper::toResponse)
                .collect(Collectors.toList());
    }
}
