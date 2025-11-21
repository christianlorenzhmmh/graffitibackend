package com.graffiti.backend.controller;

import com.graffiti.backend.dto.AllDataResponse;
import com.graffiti.backend.dto.GraffitiDataRequest;
import com.graffiti.backend.dto.PhotoUploadResponse;
import com.graffiti.backend.entity.GraffitiData;
import com.graffiti.backend.service.GraffitiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class GraffitiController {
    
    @Autowired
    private GraffitiService graffitiService;
    
    @PostMapping("/uploadGraffitiPhoto")
    public ResponseEntity<PhotoUploadResponse> uploadGraffitiPhoto(
            @RequestParam("file") MultipartFile file) {
        try {
            Long photoId = graffitiService.uploadPhoto(file);
            return ResponseEntity.ok(new PhotoUploadResponse(photoId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping("/add-graffiti-data")
    public ResponseEntity<GraffitiData> addGraffitiData(
            @RequestBody GraffitiDataRequest request) {
        try {
            GraffitiData savedData = graffitiService.addGraffitiData(request);
            return ResponseEntity.ok(savedData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/loadAllData")
    public ResponseEntity<AllDataResponse> loadAllData() {
        try {
            AllDataResponse response = graffitiService.loadAllData();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
