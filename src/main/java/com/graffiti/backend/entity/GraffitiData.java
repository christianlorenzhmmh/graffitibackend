package com.graffiti.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "graffiti_data")
public class GraffitiData {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "title")
    private String title;
    
    @Column(name = "photo_id")
    private Long photoId;
    
    @Column(name = "tags")
    private String tags;
    
    @Column(name = "latitude")
    private Double latitude;
    
    @Column(name = "longitude")
    private Double longitude;
    
    @Column(name = "altitude")
    private Double altitude;
    
    public GraffitiData() {
    }
    
    public GraffitiData(String title, Long photoId, String tags, Double latitude, Double longitude, Double altitude) {
        this.title = title;
        this.photoId = photoId;
        this.tags = tags;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public Long getPhotoId() {
        return photoId;
    }
    
    public void setPhotoId(Long photoId) {
        this.photoId = photoId;
    }
    
    public String getTags() {
        return tags;
    }
    
    public void setTags(String tags) {
        this.tags = tags;
    }
    
    public Double getLatitude() {
        return latitude;
    }
    
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    
    public Double getLongitude() {
        return longitude;
    }
    
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    
    public Double getAltitude() {
        return altitude;
    }
    
    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }
}
