package com.example.backend.config;

import com.example.backend.model.Activity;
import com.example.backend.model.Employee;
import com.example.backend.model.Reservation;
import com.example.backend.model.Shift;
import com.example.backend.repository.ReservationRepository;
import com.example.backend.repository.ShiftRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class InitData implements CommandLineRunner {

    private final ShiftRepository shiftRepository;
    private final ReservationRepository reservationRepository;

    public InitData(ShiftRepository shiftRepository, ReservationRepository reservationRepository) {
        this.shiftRepository = shiftRepository;
        this.reservationRepository = reservationRepository;
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
        goKart.setPrice(599);
        goKart.setMinAge(12);
        goKart.setMinHeight(140);
        goKart.setMaxParticipant(8);

        Activity paintball = new Activity();
        paintball.setName("Paintball");
        paintball.setDescription("Team-based paintball experience");
        paintball.setPrice(499);
        paintball.setMinAge(16);
        paintball.setMinHeight(150);
        paintball.setMaxParticipant(10);

        Activity laserTag = new Activity();
        laserTag.setName("LaserTag");
        laserTag.setDescription("Indoor laser tag arena");
        laserTag.setPrice(249);
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

        // Reservations
        Reservation reservation1 = new Reservation();
        reservation1.setActivity(goKart);
        reservation1.setConfirmed(false);
        reservation1.setParticipants(4);
        reservation1.setStartTime(LocalDateTime.of(2025,10,25,14,0));
        reservation1.setEndTime(LocalDateTime.of(2025,10,25,16,0));
        reservation1.setContactName("Joe Smith");
        reservation1.setContactEmail("jsmith@gmail.com");
        reservation1.setBookingCode("RKHL12AS69L6");
        reservation1.setContactPhone("56658778");

        Reservation reservation2 = new Reservation();
        reservation2.setActivity(paintball);
        reservation2.setConfirmed(true);
        reservation2.setParticipants(8);
        reservation2.setStartTime(LocalDateTime.of(2025, 10, 26, 11, 0));
        reservation2.setEndTime(LocalDateTime.of(2025, 10, 26, 13, 30));
        reservation2.setContactName("Alice Johnson");
        reservation2.setContactEmail("alice.johnson@example.com");
        reservation2.setBookingCode("PBLL34XZQW78");
        reservation2.setContactPhone("55512345");

        Reservation reservation3 = new Reservation();
        reservation3.setActivity(laserTag);
        reservation3.setConfirmed(false);
        reservation3.setParticipants(5);
        reservation3.setStartTime(LocalDateTime.of(2025, 10, 27, 16, 0));
        reservation3.setEndTime(LocalDateTime.of(2025, 10, 27, 18, 0));
        reservation3.setContactName("Michael Lee");
        reservation3.setContactEmail("mlee@example.com");
        reservation3.setBookingCode("LSTG89YUOP12");
        reservation3.setContactPhone("55567890");

        Reservation reservation4 = new Reservation();
        reservation4.setActivity(goKart);
        reservation4.setConfirmed(true);
        reservation4.setParticipants(3);
        reservation4.setStartTime(LocalDateTime.of(2025, 10, 28, 9, 0));
        reservation4.setEndTime(LocalDateTime.of(2025, 10, 28, 11, 0));
        reservation4.setContactName("Sophia Martinez");
        reservation4.setContactEmail("smartinez@example.com");
        reservation4.setBookingCode("GKRT56TYUI90");
        reservation4.setContactPhone("55598765");

        reservationRepository.saveAll(List.of(reservation1, reservation2, reservation3, reservation4));
    }
}