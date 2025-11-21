package com.graffiti.backend.dto;

public class GraffitiPhotoDTO {
    private Long id;
    private String fileName;
    private String contentType;
    private byte[] photoData;
    
    public GraffitiPhotoDTO() {
    }
    
    public GraffitiPhotoDTO(Long id, String fileName, String contentType, byte[] photoData) {
        this.id = id;
        this.fileName = fileName;
        this.contentType = contentType;
        this.photoData = photoData;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getContentType() {
        return contentType;
    }
    
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    public byte[] getPhotoData() {
        return photoData;
    }
    
    public void setPhotoData(byte[] photoData) {
        this.photoData = photoData;
    }
}
