package com.example.backend.service;

import com.example.backend.model.Reservation;

import java.util.List;

public interface ReservationService {
    public void createReservation(Reservation reservation);
    public void cancelReservationById(Long reservationId);
    public void updateReservation(Long reservationId, Reservation updatedReservation);
    public Reservation getReservationById(Long id);
    public List<Reservation> getAllReservations();
}
