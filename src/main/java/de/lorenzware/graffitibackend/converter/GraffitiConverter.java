package de.lorenzware.graffitibackend.converter;


import de.lorenzware.graffitibackend.dto.GraffitiDto;
import de.lorenzware.graffitibackend.entity.GraffitiEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GraffitiConverter {
    public GraffitiDto toDto(GraffitiEntity entity) {
        if (entity == null) {
            return null;
        }
        return new GraffitiDto(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getTag(),
                entity.getLatitude().toString(),
                entity.getLongitude().toString(),
                entity.getAltitude().toString(),
                entity.getPhotoPath()
        );
    }

    public List<GraffitiDto> toDtoList(java.util.List<GraffitiEntity> entities) {
        if (entities == null) {
            return java.util.Collections.emptyList();
        }
        List<GraffitiDto> dtoList = new java.util.ArrayList<>();
        for (GraffitiEntity entity : entities) {
            dtoList.add(toDto(entity));
        }
        return dtoList;
    }

}