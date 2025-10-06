package com.example.backend.controller;

import com.example.backend.dto.ReservationDTO;
import com.example.backend.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/api/reservation")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationDTO.ReservationResponse> createReservation(@RequestBody ReservationDTO.CreateReservationRequest newReservation) {
        ReservationDTO.ReservationResponse createdReservation = reservationService.createReservation(newReservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
    }

    @DeleteMapping("/{bookingCode}")
    public ResponseEntity<Void> deleteReservation(@PathVariable String bookingCode) {
        try {
            reservationService.cancelReservationByCode(bookingCode);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @GetMapping
    public ResponseEntity<?> listForDate(@RequestParam(value = "date", required = false) String dateStr) {
        LocalDate date = (dateStr == null || dateStr.isBlank()) ? LocalDate.now() : LocalDate.parse(dateStr);

        List<ReservationDTO.ReservationResponse> list = reservationService.getReservationsForDate(date);
        if (list.isEmpty()) {
            return ResponseEntity.ok("No reservations today");
        }
        return ResponseEntity.ok(list);
    }

    @PostMapping("/{bookingCode}/confirm")
    public ResponseEntity<Void> confirm(@PathVariable String bookingCode) {
        reservationService.confirmByBookingCode(bookingCode);
        return ResponseEntity.noContent().build();
    }
}
