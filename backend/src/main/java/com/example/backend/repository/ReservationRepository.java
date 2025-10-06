package com.example.backend.repository;

import com.example.backend.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, JpaSpecificationExecutor<Reservation> {
    Optional<Reservation> findByBookingCode(String bookingCode);
    List<Reservation> findAllByStartTimeBetweenAndConfirmedFalseOrderByStartTimeAsc(LocalDateTime from, LocalDateTime to);
}
