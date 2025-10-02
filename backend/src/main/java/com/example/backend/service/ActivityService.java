package com.example.backend.service;

import com.example.backend.dto.ActivityDTO;

import java.util.List;

public interface ActivityService {
    ActivityDTO.activityDto createActivity(ActivityDTO.activityDto activityDto);
    List<ActivityDTO.activityDto> getAllActivities();
    // TODO ActivityDTO.activityDto getActivityById(Long id);



}
