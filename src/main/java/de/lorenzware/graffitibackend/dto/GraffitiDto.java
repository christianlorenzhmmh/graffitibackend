package de.lorenzware.graffitibackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GraffitiDto {
    private Long id;
    private String title;
    private String description;
    private String tag;
    private String latitude;
    private String longitude;
    private String altitude;
    private String photoPath;
}