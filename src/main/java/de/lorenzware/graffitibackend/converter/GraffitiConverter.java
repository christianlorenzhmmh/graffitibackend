package de.lorenzware.graffitibackend.converter;


import de.lorenzware.graffitibackend.dto.GraffitiDto;
import de.lorenzware.graffitibackend.entity.GraffitiEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GraffitiConverter {
    public GraffitiDto toDto(GraffitiEntity ge) {
        if (ge == null) {
            return null;
        }
        String tagValue = ge.getTag() != null ? ge.getTag().getValue() : null;
        return new GraffitiDto(
                ge.getId(),
                ge.getTitle(),
                ge.getDescription(),
                tagValue,
                ge.getLatitude() != null ? ge.getLatitude().toString() : null,
                ge.getLongitude() != null ? ge.getLongitude().toString() : null,
                ge.getAltitude() != null ? ge.getAltitude().toString() : null,
                ge.getPhotoPath()
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