package com.graffiti.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "graffiti_photo")
public class GraffitiPhoto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Lob
    @Column(name = "photo_data", columnDefinition = "BLOB")
    private byte[] photoData;
    
    @Column(name = "file_name")
    private String fileName;
    
    @Column(name = "content_type")
    private String contentType;
    
    public GraffitiPhoto() {
    }
    
    public GraffitiPhoto(byte[] photoData, String fileName, String contentType) {
        this.photoData = photoData;
        this.fileName = fileName;
        this.contentType = contentType;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public byte[] getPhotoData() {
        return photoData;
    }
    
    public void setPhotoData(byte[] photoData) {
        this.photoData = photoData;
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
