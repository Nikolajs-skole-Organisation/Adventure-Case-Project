package com.example.backend.config;

import com.example.backend.model.Activity;
import com.example.backend.repository.ActivityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class InitData implements CommandLineRunner {


    private final ActivityRepository activityRepository;

    public InitData(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public void run(String... args) {


        // --- Activity ---
        Activity goKart = new Activity();
        goKart.setName("GoKart");
        goKart.setDescription("Outdoor GoKart racing track");
        goKart.setMinAge(12);
        goKart.setMinHeight(140);
        goKart.setMaxParticipant(8);
        activityRepository.save(goKart);


    }
}