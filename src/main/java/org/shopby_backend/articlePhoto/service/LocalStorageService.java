package org.shopby_backend.articlePhoto.service;

import org.shopby_backend.articlePhoto.model.StorageService;
import org.shopby_backend.exception.articlePhoto.ArticlePhotoUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class LocalStorageService implements StorageService {
    private final Path rootDir;

    public LocalStorageService(@Value("${app.upload.dir}") String uploadDir) {
        /// permet de récupérer le chemin des images
        this.rootDir= Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    @Override
    public String upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ArticlePhotoUploadException("Fichier vide");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new ArticlePhotoUploadException("Le fichier doit être une image");
        }
        String originalFilename = file.getOriginalFilename();
        String extension = getExtension(originalFilename);
        String fileName = UUID.randomUUID() + extension;
        Path dir = rootDir.resolve("articles");
        Path target = dir.resolve(fileName).normalize();

        try {
            Files.createDirectories(dir);
            if (!target.startsWith(dir)) {
                throw new ArticlePhotoUploadException("Chemin invalide");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
            }
            return "/uploads/articles/" + fileName;
        } catch (IOException e) {
            throw new ArticlePhotoUploadException("Erreur sauvegarde fichier");
        }
    }

    private String getExtension(String filename) {
        if (filename == null) return "";
        int idx = filename.lastIndexOf('.');
        if (idx == -1) return "";
        return filename.substring(idx).toLowerCase();
    }
}
