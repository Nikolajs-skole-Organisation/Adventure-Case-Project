package com.example.backend.config;

import com.example.backend.model.Activity;
import com.example.backend.model.Employee;
import com.example.backend.model.Shift;
import com.example.backend.repository.ActivityRepository;
import com.example.backend.repository.EmployeeRepository;
import com.example.backend.repository.ShiftRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class InitData implements CommandLineRunner {

    private final ShiftRepository shiftRepository;
    private final EmployeeRepository employeeRepository;
    private final ActivityRepository activityRepository;

    public InitData(ShiftRepository shiftRepository,
                    EmployeeRepository employeeRepository,
                    ActivityRepository activityRepository) {
        this.shiftRepository = shiftRepository;
        this.employeeRepository = employeeRepository;
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

//
    }
}