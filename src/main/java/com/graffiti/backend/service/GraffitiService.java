package com.graffiti.backend.service;

import com.graffiti.backend.dto.*;
import com.graffiti.backend.entity.GraffitiData;
import com.graffiti.backend.entity.GraffitiPhoto;
import com.graffiti.backend.repository.GraffitiDataRepository;
import com.graffiti.backend.repository.GraffitiPhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GraffitiService {
    
    @Autowired
    private GraffitiPhotoRepository photoRepository;
    
    @Autowired
    private GraffitiDataRepository dataRepository;
    
    public Long uploadPhoto(MultipartFile file) throws IOException {
        GraffitiPhoto photo = new GraffitiPhoto(
            file.getBytes(),
            file.getOriginalFilename(),
            file.getContentType()
        );
        GraffitiPhoto savedPhoto = photoRepository.save(photo);
        return savedPhoto.getId();
    }
    
    public GraffitiData addGraffitiData(GraffitiDataRequest request) {
        GraffitiPhoto photo = photoRepository.findById(request.getPhotoId())
            .orElseThrow(() -> new RuntimeException("Photo not found with id: " + request.getPhotoId()));
        
        GraffitiData data = new GraffitiData(
            request.getTitle(),
            photo,
            request.getTags(),
            request.getLatitude(),
            request.getLongitude(),
            request.getAltitude()
        );
        return dataRepository.save(data);
    }
    
    public AllDataResponse loadAllData() {
        List<GraffitiPhoto> photos = photoRepository.findAll();
        List<GraffitiData> dataList = dataRepository.findAll();
        
        List<GraffitiPhotoSummaryDTO> photoDTOs = photos.stream()
            .map(p -> new GraffitiPhotoSummaryDTO(p.getId(), p.getFileName(), p.getContentType()))
            .collect(Collectors.toList());
        
        List<GraffitiMetadataDTO> metadataDTOs = dataList.stream()
            .map(d -> new GraffitiMetadataDTO(d.getId(), d.getTitle(), 
                d.getPhoto() != null ? d.getPhoto().getId() : null, d.getTags(), 
                d.getLatitude(), d.getLongitude(), d.getAltitude()))
            .collect(Collectors.toList());
        
        return new AllDataResponse(photoDTOs, metadataDTOs);
    }
}
