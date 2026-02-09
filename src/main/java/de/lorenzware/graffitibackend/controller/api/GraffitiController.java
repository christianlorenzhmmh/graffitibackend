package de.lorenzware.graffitibackend.controller.api;

import de.lorenzware.graffitibackend.dto.ApiResponse;
import de.lorenzware.graffitibackend.dto.LoadGraffitiResponse;
import de.lorenzware.graffitibackend.entity.GraffitiEntity;
import de.lorenzware.graffitibackend.service.GraffitiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class GraffitiController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GraffitiController.class);

    private final GraffitiService graffitiService;

    @PostMapping(value = "/graffiti")
    public ResponseEntity<ApiResponse<GraffitiEntity>> createGraffiti(
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "latitude", required = false) BigDecimal latitude,
            @RequestParam(value = "longitude", required = false) BigDecimal longitude,
            @RequestParam(value = "altitude", required = false) BigDecimal altitude,
//            @RequestParam(value = "status", required = false, defaultValue = "reported") String status,
            @RequestParam(value = "photo", required = false) MultipartFile photo) throws IOException {

        GraffitiEntity graffitiEntity = new GraffitiEntity();
        graffitiEntity.setTitle(title);
        graffitiEntity.setDescription(description);
        graffitiEntity.setTag(tag);
        graffitiEntity.setLatitude(latitude);
        graffitiEntity.setLongitude(longitude);
        graffitiEntity.setAltitude(altitude);
//        graffiti.setStatus(status);

        GraffitiEntity savedGraffitiEntity = graffitiService.createGraffiti(graffitiEntity, photo);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, savedGraffitiEntity));
    }

//    @GetMapping(value = "/api/all-graffiti/")
//    public ResponseEntity<ApiResponse<Map<String, Object>>> getAllGraffiti(
//            @RequestParam(value = "status", required = false) String status,
//            @RequestParam(value = "page", defaultValue = "0") int page,
//            @RequestParam(value = "size", defaultValue = "100") int size) {
//
//        Page<Graffiti> graffitiPage = graffitiService.getAllGraffiti(status, page, size);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("data", graffitiPage.getContent());
//        response.put("count", graffitiPage.getContent().size());
//        response.put("totalElements", graffitiPage.getTotalElements());
//        response.put("totalPages", graffitiPage.getTotalPages());
//        response.put("currentPage", graffitiPage.getNumber());
//
//        return ResponseEntity.ok(new ApiResponse<>(true, response));
//    }

    @GetMapping(value = "/graffiti-in-area")
    public ResponseEntity<LoadGraffitiResponse> loadGraffitiInArea(
            @RequestParam double upperLeftLatitude,
            @RequestParam double upperLeftLongitude,
            @RequestParam double lowerRightLatitude,
            @RequestParam double lowerRightLongitude,
            @RequestParam int max)  throws IOException {

        LoadGraffitiResponse response = graffitiService.loadGraffitiInArea(
                upperLeftLatitude, upperLeftLongitude,
                lowerRightLatitude, lowerRightLongitude,
                max
        );
        log.info("Loaded graffiti response: {}", response);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/graffiti/{id}")
    public ResponseEntity<ApiResponse<GraffitiEntity>> getGraffitiById(@PathVariable Long id) {
        GraffitiEntity graffitiEntity = graffitiService.getGraffitiById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, graffitiEntity));
    }

    @PutMapping("/graffiti/{id}")
    public ResponseEntity<ApiResponse<GraffitiEntity>> updateGraffiti(
            @PathVariable Long id,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "latitude", required = false) BigDecimal latitude,
            @RequestParam(value = "longitude", required = false) BigDecimal longitude,
//            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "photo", required = false) MultipartFile photo) throws IOException {

        GraffitiEntity graffitiEntity = new GraffitiEntity();
        graffitiEntity.setTitle(title);
        graffitiEntity.setDescription(description);
        graffitiEntity.setTag(tag);
        graffitiEntity.setLatitude(latitude);
        graffitiEntity.setLongitude(longitude);
//        graffiti.setStatus(status);

        GraffitiEntity updatedGraffitiEntity = graffitiService.updateGraffiti(id, graffitiEntity, photo);
        return ResponseEntity.ok(new ApiResponse<>(true, updatedGraffitiEntity));
    }

    @DeleteMapping("/graffiti/{id}")
    public ResponseEntity<ApiResponse<String>> deleteGraffiti(@PathVariable Long id) {
        graffitiService.deleteGraffiti(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Graffiti entry deleted successfully"));
    }
}
