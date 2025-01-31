package fr.jerem.chaotop_backend.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

/**
 * Interface for storing images
 * This service provides methods for storing images
 */
public interface StorageService {
    
    public String uploadImage(MultipartFile file) throws IOException;
}
