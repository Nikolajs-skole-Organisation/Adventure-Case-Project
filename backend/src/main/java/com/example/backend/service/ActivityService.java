package com.example.backend.service;

import com.example.backend.dto.ActivityDTO;

import java.util.List;

public interface ActivityService {
    ActivityDTO.activityDto createActivity(ActivityDTO.activityDto activityDto);
    List<ActivityDTO.activityDto> getAllActivities();
    ActivityDTO.activityDto getActivityById(Long id);
    ActivityDTO.activityDto updateActivity(Long id, ActivityDTO.activityDto activityDto);
    void deleteActivity(Long id);
}
