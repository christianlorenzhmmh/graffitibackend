package com.graffiti.backend.dto;

import java.util.List;

public class AllDataResponse {
    private List<GraffitiPhotoSummaryDTO> graffitiPhotos;
    private List<GraffitiMetadataDTO> graffitiMetadata;
    
    public AllDataResponse() {
    }
    
    public AllDataResponse(List<GraffitiPhotoSummaryDTO> graffitiPhotos, List<GraffitiMetadataDTO> graffitiMetadata) {
        this.graffitiPhotos = graffitiPhotos;
        this.graffitiMetadata = graffitiMetadata;
    }
    
    public List<GraffitiPhotoSummaryDTO> getGraffitiPhotos() {
        return graffitiPhotos;
    }
    
    public void setGraffitiPhotos(List<GraffitiPhotoSummaryDTO> graffitiPhotos) {
        this.graffitiPhotos = graffitiPhotos;
    }
    
    public List<GraffitiMetadataDTO> getGraffitiMetadata() {
        return graffitiMetadata;
    }
    
    public void setGraffitiMetadata(List<GraffitiMetadataDTO> graffitiMetadata) {
        this.graffitiMetadata = graffitiMetadata;
    }
}
