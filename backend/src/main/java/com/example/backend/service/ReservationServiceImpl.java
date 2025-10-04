package com.example.backend.service;

import com.example.backend.model.BookingCodeGenerator;
import com.example.backend.model.Reservation;
import com.example.backend.repository.ReservationRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService{

    private final ReservationRepository reservationRepository;
    private final BookingCodeGenerator codeGen;

    public ReservationServiceImpl(ReservationRepository reservationRepository, BookingCodeGenerator codeGenerator) {
        this.reservationRepository = reservationRepository;
        this.codeGen = codeGenerator;
    }

    @Override
    public Reservation createReservation(Reservation newReservation) {
        newReservation.setId(null);

        final int maxAttempts = 5;
        for (int attempt = 0; attempt <= maxAttempts; attempt++){
            newReservation.setBookingCode(codeGen.generate());
            try{
                return reservationRepository.save(newReservation);
            } catch (DataIntegrityViolationException e){
                if (attempt == maxAttempts) throw e;
            }
        }
        throw new IllegalStateException("Unreachable");
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
