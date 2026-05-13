package in.maisonnoir.backend.api.product.service.impl;

import in.maisonnoir.backend.api.common.exception.DuplicateResourceException;
import in.maisonnoir.backend.api.common.exception.ResourceNotFoundException;
import in.maisonnoir.backend.api.product.model.dto.ProductRequestDTO;
import in.maisonnoir.backend.api.product.model.dto.ProductResponseDTO;
import in.maisonnoir.backend.api.product.model.dto.VariantItemDTO;
import in.maisonnoir.backend.api.product.repository.ProductDAO;
import in.maisonnoir.backend.api.product.repository.ProductVariantDAO;
import in.maisonnoir.backend.api.product.mapper.ProductMapper;
import in.maisonnoir.backend.api.product.model.entity.ProductEntity;
import in.maisonnoir.backend.api.product.model.entity.ProductVariantEntity;
import in.maisonnoir.backend.api.product.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductDAO productDAO;
    private final ProductVariantDAO variantDAO;

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        ProductEntity entity = ProductMapper.toEntity(productRequestDTO);
        ProductEntity saved = productDAO.save(entity);

        // Create variant items (SKUs) if provided
        List<ProductVariantEntity> savedVariants = createVariants(
                saved.getId(),
                saved.getCategory(),
                productRequestDTO.getVariantItems());

        log.info("Product created with id: {} and {} variants", saved.getId(), savedVariants.size());
        return ProductMapper.toResponse(saved, savedVariants);
    }

    @Override
    public ProductResponseDTO updateProduct(String productId, ProductRequestDTO productRequestDTO) {
        ProductEntity entity = productDAO.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        // Check if updating name to an existing product name
        if (productRequestDTO.getName() != null
                && !productRequestDTO.getName().equals(entity.getName())
                && productDAO.existsByName(productRequestDTO.getName())) {
            throw new DuplicateResourceException(
                    "Product",
                    "name",
                    productRequestDTO.getName(),
                    "Product with this name already exists");
        }

        ProductMapper.applyUpdate(productRequestDTO, entity);
        ProductEntity updated = productDAO.save(entity);

        // If new variant items are provided, replace existing ones
        List<ProductVariantEntity> variants;
        if (productRequestDTO.getVariantItems() != null && !productRequestDTO.getVariantItems().isEmpty()) {
            variantDAO.deleteByProductId(productId);
            variants = createVariants(productId, updated.getCategory(), productRequestDTO.getVariantItems());
        } else {
            variants = variantDAO.findByProductId(productId);
        }

        log.info("Product updated with id: {}", productId);
        return ProductMapper.toResponse(updated, variants);
    }

    @Override
    public void deleteProduct(String productId) {
        productDAO.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        // Delete all associated variants first
        variantDAO.deleteByProductId(productId);
        productDAO.deleteById(productId);

        log.info("Product deleted with id: {} (variants removed)", productId);
    }

    @Override
    public ProductResponseDTO getProductById(String productId) {
        ProductEntity entity = productDAO.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        List<ProductVariantEntity> variants = variantDAO.findByProductId(productId);

        log.info("Fetched product with id: {}", productId);
        return ProductMapper.toResponse(entity, variants);
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        List<ProductEntity> products = productDAO.findAll();

        log.info("Fetched all products: {} items", products.size());
        return products.stream()
                .map(product -> {
                    List<ProductVariantEntity> variants = variantDAO.findByProductId(product.getId());
                    return ProductMapper.toResponse(product, variants);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDTO> getProductsByCategory(String category) {
        List<ProductEntity> products = productDAO.findByCategory(category);

        log.info("Fetched products by category '{}': {} items", category, products.size());
        return products.stream()
                .map(product -> {
                    List<ProductVariantEntity> variants = variantDAO.findByProductId(product.getId());
                    return ProductMapper.toResponse(product, variants);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDTO> searchProductsByName(String name) {
        List<ProductEntity> products = productDAO.findByNameContainingIgnoreCase(name);

        log.info("Searched products by name '{}': {} items found", name, products.size());
        return products.stream()
                .map(product -> {
                    List<ProductVariantEntity> variants = variantDAO.findByProductId(product.getId());
                    return ProductMapper.toResponse(product, variants);
                })
                .collect(Collectors.toList());
    }

    // ─────────── Private Helpers ───────────

    private List<ProductVariantEntity> createVariants(String productId, String category, List<VariantItemDTO> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return Collections.emptyList();
        }

        return dtos.stream()
                .map(dto -> {
                    ProductVariantEntity variant = ProductMapper.toVariantEntity(dto, productId, category);
                    return variantDAO.save(variant);
                })
                .collect(Collectors.toList());
    }
}
