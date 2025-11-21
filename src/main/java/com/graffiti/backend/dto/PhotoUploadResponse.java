package com.graffiti.backend.dto;

public class PhotoUploadResponse {
    private Long photoId;
    
    public PhotoUploadResponse() {
    }
    
    public PhotoUploadResponse(Long photoId) {
        this.photoId = photoId;
    }
    
    public Long getPhotoId() {
        return photoId;
    }
    
    public void setPhotoId(Long photoId) {
        this.photoId = photoId;
    }
}
