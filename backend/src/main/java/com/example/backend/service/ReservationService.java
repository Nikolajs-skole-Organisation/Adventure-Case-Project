package com.example.backend.service;

import com.example.backend.dto.ReservationDTO;
import com.example.backend.model.Reservation;

import java.util.List;

public interface ReservationService {
    public ReservationDTO.ReservationResponse createReservation(ReservationDTO.CreateReservationRequest reservationDto);
    public void cancelReservationById(Long reservationId);
    public void updateReservation(Long reservationId, Reservation updatedReservation);
    public Reservation getReservationById(Long id);
    public List<Reservation> getAllReservations();
}
