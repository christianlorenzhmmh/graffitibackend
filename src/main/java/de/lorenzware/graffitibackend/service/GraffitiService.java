package de.lorenzware.graffitibackend.service;

import de.lorenzware.graffitibackend.converter.GraffitiConverter;
import de.lorenzware.graffitibackend.dto.GraffitiDto;
import de.lorenzware.graffitibackend.dto.LoadGraffitiResponse;
import de.lorenzware.graffitibackend.dto.TagCountDto;
import de.lorenzware.graffitibackend.entity.GraffitiEntity;
import de.lorenzware.graffitibackend.entity.TagEntity;
import de.lorenzware.graffitibackend.exception.ResourceNotFoundException;
import de.lorenzware.graffitibackend.repository.GraffitiRepository;
import de.lorenzware.graffitibackend.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static de.lorenzware.graffitibackend.dto.LoadGraffitiResponse.RESPONSE_CODE_MORE_THAN_MAX;

@Service
@RequiredArgsConstructor
@Slf4j
public class GraffitiService {

    private final GraffitiRepository graffitiRepository;
    private final TagRepository tagRepository;
    private final GraffitiConverter graffitiConverter;



    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Transactional
    public GraffitiEntity createGraffiti(GraffitiEntity graffitiEntity, String tagValue, MultipartFile photo) throws IOException {
        // TagEntity zuordnen oder neu anlegen
        if (tagValue != null) {
            TagEntity tagEntity = tagRepository.findByValue(tagValue)
                    .orElseGet(() -> {
                        TagEntity newTag = new TagEntity();
                        newTag.setValue(tagValue);
                        return tagRepository.save(newTag);
                    });
            graffitiEntity.setTag(tagEntity);
        } else {
            graffitiEntity.setTag(null);
        }
        if (photo != null && !photo.isEmpty()) {
            String photoPath = savePhoto(photo);
            graffitiEntity.setPhotoPath(photoPath);
        }
        return graffitiRepository.save(graffitiEntity);
    }

//    public Page<Graffiti> getAllGraffiti(String status, int page, int size) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
//
//        if (status != null && !status.isEmpty()) {
//            return graffitiRepository.findByStatus(status, pageable);
//        }
//        return graffitiRepository.findAll(pageable);
//    }

    public GraffitiEntity getGraffitiById(Long id) {
        return graffitiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Graffiti entry not found with id: " + id));
    }

    @Transactional
    public GraffitiEntity updateGraffiti(Long id, GraffitiEntity updatedGraffitiEntity, String tagValue, MultipartFile photo) throws IOException {
        GraffitiEntity existingGraffitiEntity = getGraffitiById(id);
        if (updatedGraffitiEntity.getTitle() != null) {
            existingGraffitiEntity.setTitle(updatedGraffitiEntity.getTitle());
        }
        if (updatedGraffitiEntity.getDescription() != null) {
            existingGraffitiEntity.setDescription(updatedGraffitiEntity.getDescription());
        }
        if (tagValue != null) {
            TagEntity tagEntity = tagRepository.findByValue(tagValue)
                    .orElseGet(() -> {
                        TagEntity newTag = new TagEntity();
                        newTag.setValue(tagValue);
                        return tagRepository.save(newTag);
                    });
            existingGraffitiEntity.setTag(tagEntity);
        }
        if (updatedGraffitiEntity.getLatitude() != null) {
            existingGraffitiEntity.setLatitude(updatedGraffitiEntity.getLatitude());
        }
        if (updatedGraffitiEntity.getLongitude() != null) {
            existingGraffitiEntity.setLongitude(updatedGraffitiEntity.getLongitude());
        }
//        if (updatedGraffiti.getStatus() != null) {
//            existingGraffiti.setStatus(updatedGraffiti.getStatus());
//        }

        if (photo != null && !photo.isEmpty()) {
            // Delete old photo if exists
            if (existingGraffitiEntity.getPhotoPath() != null) {
                deletePhoto(existingGraffitiEntity.getPhotoPath());
            }
            // Save new photo
            String photoPath = savePhoto(photo);
            existingGraffitiEntity.setPhotoPath(photoPath);
        }

        return graffitiRepository.save(existingGraffitiEntity);
    }

    @Transactional
    public void deleteGraffiti(Long id) {
        GraffitiEntity graffitiEntity = getGraffitiById(id);
        
        // Delete photo file if exists
        if (graffitiEntity.getPhotoPath() != null) {
            deletePhoto(graffitiEntity.getPhotoPath());
        }
        
        graffitiRepository.delete(graffitiEntity);
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
            log.error("Error deleting photo: {}", e.getMessage(), e);
        }
    }

    public LoadGraffitiResponse loadGraffitiInArea(
            double upperLeftLat, double upperLeftLon,
            double lowerRightLat, double lowerRightLon,
            int max) {

        List<GraffitiEntity> graffitiEntityList = graffitiRepository.findGraffitiInRectangle(
                Math.min(upperLeftLat, lowerRightLat),
                Math.max(upperLeftLat, lowerRightLat),
                Math.min(upperLeftLon, lowerRightLon),
                Math.max(upperLeftLon, lowerRightLon),
                max
        );

        int responseCode = LoadGraffitiResponse.RESPONSE_CODE_OK;
        if (graffitiEntityList.isEmpty()) responseCode = LoadGraffitiResponse.RESPONSE_CODE_EMPTY;
        else if (graffitiEntityList.size() > max) responseCode = RESPONSE_CODE_MORE_THAN_MAX;

        List<GraffitiDto> dtoList = graffitiConverter.toDtoList(graffitiEntityList);

        // TagCounts berechnen
        Map<String, Long> tagCountMap = graffitiEntityList.stream()
                .filter(g -> g.getTag() != null && g.getTag().getValue() != null)
                .collect(Collectors.groupingBy(g -> g.getTag().getValue(), Collectors.counting()));
        List<TagCountDto> tagCounts = tagCountMap.entrySet().stream()
                .map(e -> new TagCountDto(e.getKey(), e.getValue().intValue()))
                .collect(Collectors.toList());

        return new LoadGraffitiResponse(responseCode, dtoList, tagCounts);
    }
}
