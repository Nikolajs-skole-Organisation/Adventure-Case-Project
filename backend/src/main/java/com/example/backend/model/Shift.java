package com.example.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private long id;
    private long activityId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int Capacity;

    public Shift(long id, long activityId, LocalDateTime startTime, LocalDateTime endTime, int capacity) {
        this.id = id;
        this.activityId = activityId;
        this.startTime = startTime;
        this.endTime = endTime;
        Capacity = capacity;
    }

    public Shift() { }

    public long getId() {
        return id;
    }

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public int getCapacity() {
        return Capacity;
    }

    public void setCapacity(int capacity) {
        Capacity = capacity;
    }
}
