package com.graffiti.backend.dto;

public class GraffitiPhotoSummaryDTO {
    private Long id;
    private String fileName;
    private String contentType;
    
    public GraffitiPhotoSummaryDTO() {
    }
    
    public GraffitiPhotoSummaryDTO(Long id, String fileName, String contentType) {
        this.id = id;
        this.fileName = fileName;
        this.contentType = contentType;
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
}
