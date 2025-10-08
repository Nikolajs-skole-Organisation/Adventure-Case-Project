package com.example.backend.service;

import com.example.backend.dto.ReservationDTO;
import com.example.backend.model.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {
    public ReservationDTO.ReservationResponse createReservation(ReservationDTO.CreateReservationRequest reservationDto);
    public ReservationDTO.ReservationResponse updateReservation(String bookingCode, ReservationDTO.CreateReservationRequest updatedReservation);
    public Reservation getReservationById(Long id);
    public List<Reservation> getAllReservations();
    public void cancelReservationByCode(String bookingCode);
    List<ReservationDTO.ReservationResponse> getReservationsForDate(LocalDate date);
    void confirmByBookingCode(String bookingCode);
    Page<Reservation> search(String query, Pageable pageable);
}
