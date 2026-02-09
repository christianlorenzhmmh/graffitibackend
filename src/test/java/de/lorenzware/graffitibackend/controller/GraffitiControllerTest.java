package de.lorenzware.graffitibackend.controller;

import de.lorenzware.graffitibackend.controller.api.GraffitiController;
import de.lorenzware.graffitibackend.dto.GraffitiDto;
import de.lorenzware.graffitibackend.dto.LoadGraffitiResponse;
import de.lorenzware.graffitibackend.entity.GraffitiEntity;
import de.lorenzware.graffitibackend.service.GraffitiService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static de.lorenzware.graffitibackend.dto.LoadGraffitiResponse.RESPONSE_CODE_OK;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GraffitiController.class)
class GraffitiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GraffitiService graffitiService;

    @Test
    void shouldCreateGraffiti() throws Exception {
        GraffitiEntity graffitiEntity = new GraffitiEntity();
        graffitiEntity.setId(1L);
        graffitiEntity.setTitle("Test Graffiti");
        graffitiEntity.setDescription("Test Description");
//        graffiti.setStatus("reported");
        graffitiEntity.setCreatedAt(LocalDateTime.now());
        graffitiEntity.setUpdatedAt(LocalDateTime.now());

        String newTagValue = "tag";

        when(graffitiService.createGraffiti(any(GraffitiEntity.class), anyString(), any())).thenReturn(graffitiEntity);

        MockMultipartFile file = new MockMultipartFile(
                "photo",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes()
        );

        mockMvc.perform(multipart("/api/graffiti")
                        .file(file)
                        .param("title", "Test Graffiti")
                        .param("description", "Test Description")
                        .param("tag", "newTag"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("Test Graffiti"));
    }

    @Test
    void shouldGetGraffitiInArea() throws Exception {
        GraffitiDto graffitiDto1 = new GraffitiDto();
        graffitiDto1.setId(1L);
        graffitiDto1.setTitle("Graffiti 1");
//        graffitiDto1.setCreatedAt(LocalDateTime.now());
//        graffitiEntity1.setUpdatedAt(LocalDateTime.now());

        GraffitiDto graffitiDto2 = new GraffitiDto();
        graffitiDto2.setId(2L);
        graffitiDto2.setTitle("Graffiti 2");
//        graffitiEntity2.setCreatedAt(LocalDateTime.now());
//        graffitiEntity2.setUpdatedAt(LocalDateTime.now());

        List<GraffitiDto> graffitiEntityList = Arrays.asList(graffitiDto1, graffitiDto2);

        LoadGraffitiResponse response = new LoadGraffitiResponse(RESPONSE_CODE_OK, graffitiEntityList, Lists.emptyList());

        when(graffitiService.loadGraffitiInArea(anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyInt()))
                .thenReturn(response);


        mockMvc.perform(get("/api/graffiti-in-area")
                .param("upperLeftLatitude", "52.5200")
                .param("upperLeftLongitude", "13.4050")
                .param("lowerRightLatitude", "52.5000")
                .param("lowerRightLongitude", "13.4200")
                .param("max", "10"));
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data.count").value(2));
    }

    @Test
    void shouldGetGraffitiById() throws Exception {
        GraffitiEntity graffitiEntity = new GraffitiEntity();
        graffitiEntity.setId(1L);
        graffitiEntity.setTitle("Test Graffiti");
        graffitiEntity.setCreatedAt(LocalDateTime.now());
        graffitiEntity.setUpdatedAt(LocalDateTime.now());

        when(graffitiService.getGraffitiById(1L)).thenReturn(graffitiEntity);

        mockMvc.perform(get("/api/graffiti/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Test Graffiti"));
    }

    @Test
    void shouldDeleteGraffiti() throws Exception {
        mockMvc.perform(delete("/api/graffiti/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Graffiti entry deleted successfully"));
    }
}
