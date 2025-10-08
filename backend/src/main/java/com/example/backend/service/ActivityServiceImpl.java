package com.example.backend.service;

import com.example.backend.dto.ActivityDTO;
import com.example.backend.dto.ActivityMapper;
import com.example.backend.exception.NotFoundException;
import com.example.backend.model.Activity;
import com.example.backend.repository.ActivityRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Override
    public ActivityDTO.activityDto getActivityById(Long id) {
        Optional<Activity> activity = activityRepository.findById(id);
        if (activity.isPresent()) {
            return activityMapper.toDto(activity.get());
        } else {
            throw new NotFoundException("Activity not found with id" + id);
        }
    }

    @Override public ActivityDTO.activityDto updateActivity(Long id, ActivityDTO.activityDto activityDto) {
        Optional<Activity> activity = activityRepository.findById(id);
        if (activity.isPresent()) {
            Activity existing = activity.get();
            existing.setName(activityDto.name());
            existing.setDescription(activityDto.description());
            existing.setMinAge(activityDto.minAge());
            existing.setMinHeight(activityDto.minHeight());
            existing.setMaxParticipant(activityDto.maxParticipants());

            Activity saved = activityRepository.save(existing);
            return activityMapper.toDto(saved);
        } else {
            throw new NotFoundException("Activity not found with id: " + id);
        }
    }


    @Override
    public void deleteActivity(Long id) {
        if (activityRepository.existsById(id)) {
            activityRepository.deleteById(id);
        } else {
            throw new NotFoundException("Activity not found with id: " + id);
        }
    }

}
