package de.lorenzware.graffitibackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagCountDto {
    private String tagValue;
    private int count;
}

