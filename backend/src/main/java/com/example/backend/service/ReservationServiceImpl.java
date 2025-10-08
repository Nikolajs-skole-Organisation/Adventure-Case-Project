package com.example.backend.service;

import com.example.backend.dto.ReservationDTO;
import com.example.backend.dto.ReservationMapper;
import com.example.backend.model.BookingCodeGenerator;
import com.example.backend.model.Reservation;
import com.example.backend.model.ReservationSpecification;
import com.example.backend.repository.ReservationRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService{

    private final ReservationRepository reservationRepository;
    private final BookingCodeGenerator codeGen;
    private final ReservationMapper reservationMapper;

    public ReservationServiceImpl(ReservationRepository reservationRepository, BookingCodeGenerator codeGenerator,
                                  ReservationMapper reservationMapper) {
        this.reservationRepository = reservationRepository;
        this.codeGen = codeGenerator;
        this.reservationMapper = reservationMapper;
    }

    @Override
    public ReservationDTO.ReservationResponse createReservation(ReservationDTO.CreateReservationRequest newReservationDto) {
        Reservation newReservation = reservationMapper.toEntity(newReservationDto);
        newReservation.setId(null);

        final int maxAttempts = 5;
        for (int attempt = 0; attempt <= maxAttempts; attempt++){
            newReservation.setBookingCode(codeGen.generate());
            try{
                Reservation savedReservation = reservationRepository.save(newReservation);
                return reservationMapper.toResponse(savedReservation);
            } catch (DataIntegrityViolationException e){
                if (attempt == maxAttempts) throw e;
            }
        }
        throw new IllegalStateException("Unreachable");
    }

    @Override
    public ReservationDTO.ReservationResponse updateReservation(String bookingCode, ReservationDTO.CreateReservationRequest updatedReservation) {
        Reservation reservationToBeUpdated = reservationRepository.findBy(bookingCode);
    }

    @Override
    public Reservation getReservationById(Long id) {
        return null;
    }

    @Override
    public List<Reservation> getAllReservations() {
        return List.of();
    }

    @Override
    public void cancelReservationByCode(String bookingCode) {
        Optional<Reservation> optional = reservationRepository.findByBookingCode(bookingCode);
        if (optional.isEmpty()){
            throw new IllegalArgumentException("No reservation could be found with Booking code: " + bookingCode);
        }
        reservationRepository.deleteById(optional.get().getId());
    }

    @Override
    public List<ReservationDTO.ReservationResponse> getReservationsForDate(LocalDate date) {
        ZoneId zone = ZoneId.of("Europe/Copenhagen");
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        return reservationRepository.findAllByStartTimeBetweenAndConfirmedFalseOrderByStartTimeAsc(start, end)
                .stream()
                .map(reservationMapper::toResponse)
                .toList();
    }

    @Override
    public void confirmByBookingCode(String bookingCode) {
        Reservation r = reservationRepository.findByBookingCode(bookingCode).orElseThrow(() -> new IllegalArgumentException("Reservation not found for code: " + bookingCode));
        if (!r.isConfirmed()) {
            r.setConfirmed(true);
            reservationRepository.save(r);
        }
    }

    @Override
    public Page<Reservation> search(String query, Pageable pageable){
        return reservationRepository.findAll(ReservationSpecification.search(query), pageable);
    }
}
