package fr.jerem.chaotop_backend.service;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import fr.jerem.chaotop_backend.exception.ImageUploadException;
import lombok.extern.slf4j.Slf4j;

/**
 * StorageService class for storing images of the application in the Cloudinary
 * storage cloud.
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

    /**
     * Upload a {@link MultipartFile} file to Cloudinary.
     * 
     * @param MultipartFile the file to be uploaded
     * @return a {@code String} url of the uploaded image
     * @throws ImageUploadException if an error occured during upload
     */
    public String uploadImage(MultipartFile file) throws ImageUploadException {
        log.debug("Cloudinary upload image...");
        try {
            if (!file.isEmpty()) {
                @SuppressWarnings("unchecked")
                Map<String, Object> uploadResult = (Map<String, Object>) cloudinary.uploader().upload(file.getBytes(),
                        ObjectUtils.emptyMap());
                return uploadResult.get("secure_url").toString();
            } else
                return null;
        } catch (Exception e) {
            throw new ImageUploadException("Failed to upload image to Cloudinary", e);
        }
    }
}
