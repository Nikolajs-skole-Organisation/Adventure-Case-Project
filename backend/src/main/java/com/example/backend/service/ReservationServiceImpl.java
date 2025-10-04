package com.example.backend.service;

import com.example.backend.model.Reservation;
import com.example.backend.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService{

    private final ReservationRepository reservationRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Reservation createReservation(Reservation newReservation) {
        newReservation.setId(null);
        return reservationRepository.save(newReservation);
    }

    @Override
    public void cancelReservationById(Long reservationId) {

    }

    @Override
    public void updateReservation(Long reservationId, Reservation updatedReservation) {

    }

    @Override
    public Reservation getReservationById(Long id) {
        return null;
    }

    @Override
    public List<Reservation> getAllReservations() {
        return List.of();
    }
}
