package com.example.backend.service;

import com.example.backend.dto.ActivityDTO;
import com.example.backend.dto.ActivityMapper;
import com.example.backend.model.Activity;
import com.example.backend.repository.ActivityRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {
    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;

    public ActivityServiceImpl(ActivityRepository activityRepository, ActivityMapper activityMapper) {
        this.activityRepository = activityRepository;
        this.activityMapper = activityMapper;
    }


    @Override
    public ActivityDTO.activityDto createActivity(ActivityDTO.activityDto activityDto) {
        Activity activity = activityMapper.toEntity(activityDto);
        activity.setId(null);
        Activity saved = activityRepository.save(activity);
        return activityMapper.toDto(saved);
    }

    @Override
    public List<ActivityDTO.activityDto> getAllActivities() {
        List<Activity> activities = activityRepository.findAll();
        List<ActivityDTO.activityDto> listOfActivities = new ArrayList<>();
        for (Activity activity : activities) {
            listOfActivities.add(activityMapper.toDto(activity));
        }
        return listOfActivities;
    }



}
