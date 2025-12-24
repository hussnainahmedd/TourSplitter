package com.example.toursplitter.model;

public class Member {
    private String memberId;
    private String tourId;
    private String userId;
    private String name;
    private String email;
    private long joinedAt;

    public Member() {}

    public Member(String memberId, String tourId, String userId, String name, String email) {
        this.memberId = memberId;
        this.tourId = tourId;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.joinedAt = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }
    public String getTourId() { return tourId; }
    public void setTourId(String tourId) { this.tourId = tourId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public long getJoinedAt() { return joinedAt; }
    public void setJoinedAt(long joinedAt) { this.joinedAt = joinedAt; }
}