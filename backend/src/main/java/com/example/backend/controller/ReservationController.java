package com.example.backend.controller;

import com.example.backend.dto.ReservationDTO;
import com.example.backend.model.Reservation;
import com.example.backend.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/reservation")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationDTO newReservation) {
        ReservationDTO createdReservation = reservationService.createReservation(newReservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
    }
}
