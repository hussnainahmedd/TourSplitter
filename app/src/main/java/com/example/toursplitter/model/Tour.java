package com.example.toursplitter.model;

import java.util.ArrayList;
import java.util.List;

public class Tour {
    private String tourId;
    private String name;
    private String type; // "local" or "international"
    private String createdBy;
    private long createdAt;
    private List<String> memberIds;

    public Tour() {
        this.memberIds = new ArrayList<>();
    }

    public Tour(String tourId, String name, String type, String createdBy) {
        this.tourId = tourId;
        this.name = name;
        this.type = type;
        this.createdBy = createdBy;
        this.createdAt = System.currentTimeMillis();
        this.memberIds = new ArrayList<>();
    }

    // Getters and Setters
    public String getTourId() { return tourId; }
    public void setTourId(String tourId) { this.tourId = tourId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    public List<String> getMemberIds() { return memberIds; }
    public void setMemberIds(List<String> memberIds) { this.memberIds = memberIds; }
}
