package de.lorenzware.graffitibackend.controller;

import de.lorenzware.graffitibackend.entity.Graffiti;
import de.lorenzware.graffitibackend.service.GraffitiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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
        Graffiti graffiti = new Graffiti();
        graffiti.setId(1L);
        graffiti.setTitle("Test Graffiti");
        graffiti.setDescription("Test Description");
        graffiti.setStatus("reported");
        graffiti.setCreatedAt(LocalDateTime.now());
        graffiti.setUpdatedAt(LocalDateTime.now());

        when(graffitiService.createGraffiti(any(Graffiti.class), any())).thenReturn(graffiti);

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
                        .param("status", "reported"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("Test Graffiti"));
    }

    @Test
    void shouldGetAllGraffiti() throws Exception {
        Graffiti graffiti1 = new Graffiti();
        graffiti1.setId(1L);
        graffiti1.setTitle("Graffiti 1");
        graffiti1.setCreatedAt(LocalDateTime.now());
        graffiti1.setUpdatedAt(LocalDateTime.now());

        Graffiti graffiti2 = new Graffiti();
        graffiti2.setId(2L);
        graffiti2.setTitle("Graffiti 2");
        graffiti2.setCreatedAt(LocalDateTime.now());
        graffiti2.setUpdatedAt(LocalDateTime.now());

        List<Graffiti> graffitiList = Arrays.asList(graffiti1, graffiti2);
        Page<Graffiti> page = new PageImpl<>(graffitiList);

        when(graffitiService.getAllGraffiti(isNull(), eq(0), eq(100))).thenReturn(page);

        mockMvc.perform(get("/api/graffiti"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.count").value(2));
    }

    @Test
    void shouldGetGraffitiById() throws Exception {
        Graffiti graffiti = new Graffiti();
        graffiti.setId(1L);
        graffiti.setTitle("Test Graffiti");
        graffiti.setCreatedAt(LocalDateTime.now());
        graffiti.setUpdatedAt(LocalDateTime.now());

        when(graffitiService.getGraffitiById(1L)).thenReturn(graffiti);

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
