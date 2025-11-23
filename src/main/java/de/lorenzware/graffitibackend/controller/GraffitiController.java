package de.lorenzware.graffitibackend.controller;

import de.lorenzware.graffitibackend.dto.ApiResponse;
import de.lorenzware.graffitibackend.entity.Graffiti;
import de.lorenzware.graffitibackend.service.GraffitiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/graffiti")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class GraffitiController {

    private final GraffitiService graffitiService;

    @PostMapping
    public ResponseEntity<ApiResponse<Graffiti>> createGraffiti(
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "latitude", required = false) BigDecimal latitude,
            @RequestParam(value = "longitude", required = false) BigDecimal longitude,
            @RequestParam(value = "status", required = false, defaultValue = "reported") String status,
            @RequestParam(value = "photo", required = false) MultipartFile photo) throws IOException {

        Graffiti graffiti = new Graffiti();
        graffiti.setTitle(title);
        graffiti.setDescription(description);
        graffiti.setLocation(location);
        graffiti.setLatitude(latitude);
        graffiti.setLongitude(longitude);
        graffiti.setStatus(status);

        Graffiti savedGraffiti = graffitiService.createGraffiti(graffiti, photo);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, savedGraffiti));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAllGraffiti(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "100") int size) {

        Page<Graffiti> graffitiPage = graffitiService.getAllGraffiti(status, page, size);
        
        Map<String, Object> response = new HashMap<>();
        response.put("data", graffitiPage.getContent());
        response.put("count", graffitiPage.getContent().size());
        response.put("totalElements", graffitiPage.getTotalElements());
        response.put("totalPages", graffitiPage.getTotalPages());
        response.put("currentPage", graffitiPage.getNumber());

        return ResponseEntity.ok(new ApiResponse<>(true, response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Graffiti>> getGraffitiById(@PathVariable Long id) {
        Graffiti graffiti = graffitiService.getGraffitiById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, graffiti));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Graffiti>> updateGraffiti(
            @PathVariable Long id,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "latitude", required = false) BigDecimal latitude,
            @RequestParam(value = "longitude", required = false) BigDecimal longitude,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "photo", required = false) MultipartFile photo) throws IOException {

        Graffiti graffiti = new Graffiti();
        graffiti.setTitle(title);
        graffiti.setDescription(description);
        graffiti.setLocation(location);
        graffiti.setLatitude(latitude);
        graffiti.setLongitude(longitude);
        graffiti.setStatus(status);

        Graffiti updatedGraffiti = graffitiService.updateGraffiti(id, graffiti, photo);
        return ResponseEntity.ok(new ApiResponse<>(true, updatedGraffiti));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteGraffiti(@PathVariable Long id) {
        graffitiService.deleteGraffiti(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Graffiti entry deleted successfully"));
    }
}
