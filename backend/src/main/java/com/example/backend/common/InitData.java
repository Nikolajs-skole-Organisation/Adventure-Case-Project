package com.example.backend.common;

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
        // --- Employees ---
        Employee alice = new Employee(null, "Alice Anders", "alice@gokart.com", "12345678", "Employee");
        Employee bob   = new Employee(null,"Bob Berg",   "bob@gokart.com",   "87654321", "Employee");

        // --- Activity ---
        Activity goKart = new Activity();
        goKart.setName("GoKart");
        goKart.setDescription("Outdoor GoKart racing track");
        goKart.setMinAge(12);
        goKart.setMinHeight(140);
        goKart.setMaxParticipant(8);
        activityRepository.save(goKart);

        // --- Shifts ---
        Shift morning = new Shift();
        morning.setActivity(goKart);
        morning.setStartTime(LocalDate.now().atTime(9, 0));
        morning.setEndTime(LocalDate.now().atTime(12, 0));
        morning.setCapacity(8);
        morning.addEmployee(alice);

        Shift afternoon = new Shift();
        afternoon.setActivity(goKart);
        afternoon.setStartTime(LocalDate.now().atTime(13, 0));
        afternoon.setEndTime(LocalDate.now().atTime(17, 0));
        afternoon.setCapacity(8);
        afternoon.addEmployee(bob);

        shiftRepository.saveAll(List.of(morning, afternoon));
    }
}