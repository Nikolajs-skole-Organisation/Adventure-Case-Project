package com.example.backend.dto;

public class ActivityDTO {
    public record activityDto(Long id,
                              String name,
                              String description,
                              int minAge,
                              int minHeight,
                              int maxParticipants){}
}
