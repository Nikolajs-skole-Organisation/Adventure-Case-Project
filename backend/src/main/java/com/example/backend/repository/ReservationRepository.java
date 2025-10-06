package com.example.backend.repository;

import com.example.backend.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByBookingCode(String bookingCode);
}
