package com.graffiti.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.graffiti.backend.dto.GraffitiDataRequest;
import com.graffiti.backend.repository.GraffitiDataRepository;
import com.graffiti.backend.repository.GraffitiPhotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GraffitiControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private GraffitiPhotoRepository photoRepository;
    
    @Autowired
    private GraffitiDataRepository dataRepository;
    
    @BeforeEach
    public void setup() {
        dataRepository.deleteAll();
        photoRepository.deleteAll();
    }
    
    @Test
    public void testUploadGraffitiPhoto() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test-photo.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            "test image content".getBytes()
        );
        
        mockMvc.perform(multipart("/api/uploadGraffitiPhoto")
                .file(file))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.photoId").exists())
            .andExpect(jsonPath("$.photoId").isNumber());
    }
    
    @Test
    public void testAddGraffitiData() throws Exception {
        // First upload a photo
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test-photo.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            "test image content".getBytes()
        );
        
        MvcResult uploadResult = mockMvc.perform(multipart("/api/uploadGraffitiPhoto")
                .file(file))
            .andExpect(status().isOk())
            .andReturn();
        
        String uploadResponse = uploadResult.getResponse().getContentAsString();
        Long photoId = objectMapper.readTree(uploadResponse).get("photoId").asLong();
        
        GraffitiDataRequest request = new GraffitiDataRequest(
            "Street Art",
            photoId,
            "urban,colorful",
            40.7128,
            -74.0060,
            10.5
        );
        
        mockMvc.perform(post("/api/add-graffiti-data")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.title").value("Street Art"))
            .andExpect(jsonPath("$.tags").value("urban,colorful"))
            .andExpect(jsonPath("$.latitude").value(40.7128))
            .andExpect(jsonPath("$.longitude").value(-74.0060))
            .andExpect(jsonPath("$.altitude").value(10.5));
    }
    
    @Test
    public void testLoadAllData() throws Exception {
        // First upload a photo
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test-photo.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            "test image content".getBytes()
        );
        
        MvcResult uploadResult = mockMvc.perform(multipart("/api/uploadGraffitiPhoto")
                .file(file))
            .andExpect(status().isOk())
            .andReturn();
        
        String uploadResponse = uploadResult.getResponse().getContentAsString();
        Long photoId = objectMapper.readTree(uploadResponse).get("photoId").asLong();
        
        // Then add metadata
        GraffitiDataRequest request = new GraffitiDataRequest(
            "Test Graffiti",
            photoId,
            "test,art",
            40.7128,
            -74.0060,
            10.5
        );
        
        mockMvc.perform(post("/api/add-graffiti-data")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());
        
        // Finally, load all data
        mockMvc.perform(get("/api/loadAllData"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.graffitiPhotos").isArray())
            .andExpect(jsonPath("$.graffitiPhotos", hasSize(1)))
            .andExpect(jsonPath("$.graffitiPhotos[0].id").value(photoId))
            .andExpect(jsonPath("$.graffitiPhotos[0].fileName").value("test-photo.jpg"))
            .andExpect(jsonPath("$.graffitiMetadata").isArray())
            .andExpect(jsonPath("$.graffitiMetadata", hasSize(1)))
            .andExpect(jsonPath("$.graffitiMetadata[0].title").value("Test Graffiti"))
            .andExpect(jsonPath("$.graffitiMetadata[0].photoId").value(photoId));
    }
    
    @Test
    public void testLoadAllDataWithNoData() throws Exception {
        mockMvc.perform(get("/api/loadAllData"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.graffitiPhotos").isArray())
            .andExpect(jsonPath("$.graffitiPhotos", hasSize(0)))
            .andExpect(jsonPath("$.graffitiMetadata").isArray())
            .andExpect(jsonPath("$.graffitiMetadata", hasSize(0)));
    }
    
    @Test
    public void testUploadGraffitiPhotoWithInvalidFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.txt",
            "text/plain",
            "not an image".getBytes()
        );
        
        mockMvc.perform(multipart("/api/uploadGraffitiPhoto")
                .file(file))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    public void testAddGraffitiDataWithInvalidCoordinates() throws Exception {
        // First upload a photo
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test-photo.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            "test image content".getBytes()
        );
        
        MvcResult uploadResult = mockMvc.perform(multipart("/api/uploadGraffitiPhoto")
                .file(file))
            .andExpect(status().isOk())
            .andReturn();
        
        String uploadResponse = uploadResult.getResponse().getContentAsString();
        Long photoId = objectMapper.readTree(uploadResponse).get("photoId").asLong();
        
        GraffitiDataRequest request = new GraffitiDataRequest(
            "Street Art",
            photoId,
            "urban,colorful",
            91.0, // Invalid latitude > 90
            -74.0060,
            10.5
        );
        
        mockMvc.perform(post("/api/add-graffiti-data")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }
}
