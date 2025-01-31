package fr.jerem.chaotop_backend.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * StorageService class for storing images of the application.
 * 
 * <p>
 * This class provides functionality for uploading an image
 * data.
 * </p>
 * 
 * 
 */
@Slf4j
public class CloudinaryStorageService implements StorageService {
    private final Cloudinary cloudinary;

    public CloudinaryStorageService(
            String cloudName,
            String apiKey,
            String apiSecret) {

        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret));
    }

    public String uploadImage(MultipartFile file) throws IOException {
        log.debug("Cloudinary upload image...");
        @SuppressWarnings("unchecked")
        Map<String, Object> uploadResult = (Map<String, Object>)cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("secure_url").toString();
    }
}
