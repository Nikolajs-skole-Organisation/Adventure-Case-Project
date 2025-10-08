package com.example.backend.config;

import com.example.backend.model.Activity;
import com.example.backend.model.Employee;
import com.example.backend.model.Shift;
import com.example.backend.repository.ShiftRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class InitData implements CommandLineRunner {

    private final ShiftRepository shiftRepository;

    public InitData(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }

    @Override
    public void run(String... args) {

        // ----- Employees -----
        Employee alice   = new Employee(null, "Alice Anders", "alice@gokart.com", "12345678", "Employee", "velkommen123");
        Employee bob     = new Employee(null, "Bob Berg", "bob@gokart.com", "87654321", "Employee", "velkommen123");
        Employee charlie = new Employee(null, "Charlie Christensen", "charlie@paintball.com", "11112222", "Employee", "velkommen123");

        // ----- Activities -----
        Activity goKart = new Activity();
        goKart.setName("GoKart");
        goKart.setDescription("Outdoor GoKart racing track");
        goKart.setMinAge(12);
        goKart.setMinHeight(140);
        goKart.setMaxParticipant(8);

        Activity paintball = new Activity();
        paintball.setName("Paintball");
        paintball.setDescription("Team-based paintball experience");
        paintball.setMinAge(16);
        paintball.setMinHeight(150);
        paintball.setMaxParticipant(10);

        Activity laserTag = new Activity();
        laserTag.setName("LaserTag");
        laserTag.setDescription("Indoor laser tag arena");
        laserTag.setMinAge(10);
        laserTag.setMinHeight(130);
        laserTag.setMaxParticipant(12);

        // --- Shifts ---
        Shift morning = new Shift();
        morning.setStartTime(LocalDate.now().atTime(9, 0));
        morning.setEndTime(LocalDate.now().atTime(12, 0));
        morning.setCapacity(10);

        Shift afternoon = new Shift();
        afternoon.setStartTime(LocalDate.now().atTime(13, 0));
        afternoon.setEndTime(LocalDate.now().atTime(17, 0));
        afternoon.setCapacity(10);

        // Link activities to shifts
        morning.addActivity(goKart);
        morning.addActivity(paintball);

        afternoon.addActivity(goKart);
        afternoon.addActivity(laserTag);

        // Assign employees to shifts
        morning.addEmployee(alice);
        afternoon.addEmployee(bob);
        afternoon.addEmployee(charlie);

        // Save shifts
        shiftRepository.saveAll(List.of(morning, afternoon));
    }
}