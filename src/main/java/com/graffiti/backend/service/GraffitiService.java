package com.graffiti.backend.service;

import com.graffiti.backend.entity.Graffiti;
import com.graffiti.backend.exception.ResourceNotFoundException;
import com.graffiti.backend.repository.GraffitiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GraffitiService {

    private final GraffitiRepository graffitiRepository;

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Transactional
    public Graffiti createGraffiti(Graffiti graffiti, MultipartFile photo) throws IOException {
        if (photo != null && !photo.isEmpty()) {
            String photoPath = savePhoto(photo);
            graffiti.setPhotoPath(photoPath);
        }
        return graffitiRepository.save(graffiti);
    }

    public Page<Graffiti> getAllGraffiti(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        
        if (status != null && !status.isEmpty()) {
            return graffitiRepository.findByStatus(status, pageable);
        }
        return graffitiRepository.findAll(pageable);
    }

    public Graffiti getGraffitiById(Long id) {
        return graffitiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Graffiti entry not found with id: " + id));
    }

    @Transactional
    public Graffiti updateGraffiti(Long id, Graffiti updatedGraffiti, MultipartFile photo) throws IOException {
        Graffiti existingGraffiti = getGraffitiById(id);

        if (updatedGraffiti.getTitle() != null) {
            existingGraffiti.setTitle(updatedGraffiti.getTitle());
        }
        if (updatedGraffiti.getDescription() != null) {
            existingGraffiti.setDescription(updatedGraffiti.getDescription());
        }
        if (updatedGraffiti.getLocation() != null) {
            existingGraffiti.setLocation(updatedGraffiti.getLocation());
        }
        if (updatedGraffiti.getLatitude() != null) {
            existingGraffiti.setLatitude(updatedGraffiti.getLatitude());
        }
        if (updatedGraffiti.getLongitude() != null) {
            existingGraffiti.setLongitude(updatedGraffiti.getLongitude());
        }
        if (updatedGraffiti.getStatus() != null) {
            existingGraffiti.setStatus(updatedGraffiti.getStatus());
        }

        if (photo != null && !photo.isEmpty()) {
            // Delete old photo if exists
            if (existingGraffiti.getPhotoPath() != null) {
                deletePhoto(existingGraffiti.getPhotoPath());
            }
            // Save new photo
            String photoPath = savePhoto(photo);
            existingGraffiti.setPhotoPath(photoPath);
        }

        return graffitiRepository.save(existingGraffiti);
    }

    @Transactional
    public void deleteGraffiti(Long id) {
        Graffiti graffiti = getGraffitiById(id);
        
        // Delete photo file if exists
        if (graffiti.getPhotoPath() != null) {
            deletePhoto(graffiti.getPhotoPath());
        }
        
        graffitiRepository.delete(graffiti);
    }

    private String savePhoto(MultipartFile photo) throws IOException {
        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename
        String originalFilename = photo.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".") 
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";
        String filename = UUID.randomUUID().toString() + extension;

        // Save file
        Path filePath = uploadPath.resolve(filename);
        Files.copy(photo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/" + filename;
    }

    private void deletePhoto(String photoPath) {
        try {
            if (photoPath != null && photoPath.startsWith("/uploads/")) {
                String filename = photoPath.replace("/uploads/", "");
                Path filePath = Paths.get(uploadDir, filename);
                Files.deleteIfExists(filePath);
            }
        } catch (IOException e) {
            // Log error but don't fail the operation
            System.err.println("Error deleting photo: " + e.getMessage());
        }
    }
}
