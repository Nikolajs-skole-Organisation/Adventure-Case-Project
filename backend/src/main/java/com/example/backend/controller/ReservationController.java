package com.example.backend.controller;

import com.example.backend.dto.ReservationDTO;
import com.example.backend.dto.ReservationPageResponse;
import com.example.backend.model.Reservation;
import com.example.backend.service.ReservationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    // The method searches for the given query. If no result can be found it will return the first 20 objects
    // and list them by startTime in ascending order, so the next reservation is first.
    // if more than one result it will list them by startTime in ascending order also.
    @GetMapping("/search")
    public ResponseEntity<ReservationPageResponse> search(
            @RequestParam(name = "query") String query,
            @PageableDefault(size = 20, sort = "startTime", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<Reservation> page = reservationService.search(query, pageable);
        var body = new ReservationPageResponse(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements()
        );
        return ResponseEntity.ok(body);
    }

    @PutMapping("/{bookingCode}")
    public ResponseEntity<ReservationDTO.ReservationResponse> updateReservation(@PathVariable String bookingCode,
                                                                                @RequestBody ReservationDTO.UpdateReservationRequest updatedReservation) {
        try {
            return ResponseEntity.ok(reservationService.updateReservation(bookingCode, updatedReservation));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}