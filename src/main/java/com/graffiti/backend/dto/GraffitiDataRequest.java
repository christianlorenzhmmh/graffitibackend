package com.graffiti.backend.dto;

public class GraffitiDataRequest {
    private String title;
    private Long photoId;
    private String tags;
    private Double latitude;
    private Double longitude;
    private Double altitude;
    
    public GraffitiDataRequest() {
    }
    
    public GraffitiDataRequest(String title, Long photoId, String tags, Double latitude, Double longitude, Double altitude) {
        this.title = title;
        this.photoId = photoId;
        this.tags = tags;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
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
