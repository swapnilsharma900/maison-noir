package in.maisonnoir.backend.api.product.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import in.maisonnoir.backend.api.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/products")
public class ProductImageController {

    private final Cloudinary cloudinary;

    public ProductImageController(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @PostMapping("/upload-image")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "File is empty", null));
        }

        // Validate file type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Only image files are allowed", null));
        }

        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "maison-noir/products",
                            "resource_type", "image",
                            "transformation", "q_auto,f_auto"   // auto quality + format
                    )
            );

            String imageUrl = uploadResult.get("secure_url").toString();
            return ResponseEntity.ok(new ApiResponse(true, "Image uploaded", imageUrl));

        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse(false, "Upload failed: " + e.getMessage(), null));
        }
    }
}
