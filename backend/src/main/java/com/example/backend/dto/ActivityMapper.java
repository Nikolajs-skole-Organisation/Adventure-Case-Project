package com.example.backend.dto;

import com.example.backend.model.Activity;
import org.springframework.stereotype.Component;

@Component
public class ActivityMapper {

    public ActivityDTO.activityDto toDto(Activity activity) {
        if (activity == null) return null;
        return new ActivityDTO.activityDto(
                activity.getId(),
                activity.getName(),
                activity.getDescription(),
                activity.getPrice(),
                activity.getMinAge(),
                activity.getMinHeight(),
                activity.getMaxParticipant());
    }

    public Activity toEntity(ActivityDTO.activityDto dto) {
        if (dto == null) return null;
        Activity a = new Activity();
        a.setId(dto.id());
        a.setName(dto.name());
        a.setDescription(dto.description());
        a.setPrice(dto.price());
        a.setMinAge(dto.minAge());
        a.setMinHeight(dto.minHeight());
        a.setMaxParticipant(dto.maxParticipants());
        return a;
    }


}
