package fr.jerem.chaotop_backend.service;


import org.springframework.web.multipart.MultipartFile;

import fr.jerem.chaotop_backend.exception.ImageUploadException;

/**
 * Interface for storing images
 * This service provides methods for storing images
 */
public interface StorageService {
    
    public String uploadImage(MultipartFile file) throws ImageUploadException ;
}
