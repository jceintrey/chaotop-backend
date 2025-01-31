package fr.jerem.chaotop_backend.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    public String uploadImage(MultipartFile file) throws IOException;
}
