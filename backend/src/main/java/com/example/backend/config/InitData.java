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
import java.util.ArrayList;
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

        if (shiftRepository.count() > 0 || reservationRepository.count() > 0) {
            return;
        }

        // ----- Employees -----
        List<Employee> employees = List.of(
                new Employee(null, "Alice Anders",       "alice@gokart.com",      "12345678", "Employee", "velkommen123"),
                new Employee(null, "Bob Berg",           "bob@gokart.com",        "87654321", "Employee", "velkommen123"),
                new Employee(null, "Charlie Christensen", "charlie@paintball.com", "11112222", "Employee", "velkommen123"),
                new Employee(null, "Ditte Dam",          "ditte@arena.com",       "22223333", "Employee", "velkommen123"),
                new Employee(null, "Erik Eskildsen",     "erik@arena.com",        "33334444", "Employee", "velkommen123"),
                new Employee(null, "Freja Frost",        "freja@lasertag.com",    "44445555", "Employee", "velkommen123"),
                new Employee(null, "Gustav Graversen",   "gustav@gokart.com",     "55556666", "Employee", "velkommen123"),
                new Employee(null, "Helle Hansen",       "helle@paintball.com",   "66667777", "Employee", "velkommen123"),
                new Employee(null, "Ida Iversen",        "ida@lasertag.com",      "77778888", "Employee", "velkommen123"),
                new Employee(null, "Jens Jensen",        "jens@arena.com",        "88889999", "Employee", "velkommen123")
        );

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

        List<Shift> shifts = new ArrayList<>();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        int empIndex = 0;
        int minStaff = 1;
        int maxStaff = 4;

        for (LocalDate d = startDate; !d.isAfter(endDate); d = d.plusDays(1)) {
            // 10–16
            Shift s1 = new Shift();
            s1.setStartTime(d.atTime(10, 0));
            s1.setEndTime(d.atTime(16, 0));
            s1.setCapacity(12);
            s1.addActivity(goKart);
            s1.addActivity(paintball);
            s1.addActivity(laserTag);

            // 16–22
            Shift s2 = new Shift();
            s2.setStartTime(d.atTime(16, 0));
            s2.setEndTime(d.atTime(22, 0));
            s2.setCapacity(12);
            s2.addActivity(goKart);
            s2.addActivity(paintball);
            s2.addActivity(laserTag);

            int staffForMorning = (int) (Math.random() * (maxStaff - minStaff + 1)) + minStaff;
            int staffForEvening = (int) (Math.random() * (maxStaff - minStaff + 1)) + minStaff;

            for (int i = 0; i < staffForMorning; i++) {
                s1.addEmployee(employees.get(empIndex % employees.size()));
                empIndex++;
            }
            for (int i = 0; i < staffForEvening; i++) {
                s2.addEmployee(employees.get(empIndex % employees.size()));
                empIndex++;
            }

            shifts.add(s1);
            shifts.add(s2);
        }

        shiftRepository.saveAll(shifts);

        // ----- Reservations -----
        Reservation reservation1 = new Reservation();
        reservation1.setActivity(goKart);
        reservation1.setConfirmed(false);
        reservation1.setParticipants(4);
        reservation1.setStartTime(LocalDateTime.of(2025, 10, 25, 14, 0));
        reservation1.setEndTime(LocalDateTime.of(2025, 10, 25, 16, 0));
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