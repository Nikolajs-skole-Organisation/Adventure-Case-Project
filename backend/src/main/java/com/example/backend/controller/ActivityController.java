package com.example.backend.controller;

import com.example.backend.dto.ActivityDTO;
import com.example.backend.service.ActivityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private ActivityService activityService; // TODO Set to final


    @PostMapping
    public ResponseEntity<ActivityDTO.activityDto> createActivity(@RequestBody ActivityDTO.activityDto activityDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(activityService.createActivity(activityDto));
    }

    @GetMapping
    public ResponseEntity<List<ActivityDTO.activityDto>> getAllActivities() {
        return ResponseEntity.ok(activityService.getAllActivities());
    }


}
