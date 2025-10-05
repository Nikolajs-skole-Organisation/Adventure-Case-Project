package com.example.backend.service;

import com.example.backend.model.BookingCodeGenerator;
import com.example.backend.model.Reservation;
import com.example.backend.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private BookingCodeGenerator codeGen;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Test
    void createReservation() {
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setStartTime(LocalDateTime.of(2025, 10, 5, 16, 30));
        reservation.setEndTime(LocalDateTime.of(2025, 10, 5, 18, 0));
        reservation.setParticipants(4);
        reservation.setContactName("Nikolaj Albrektsen");
        reservation.setContactPhone(22712123);
        reservation.setContactEmail("nikolaja12@hotmail.com");

        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(invocation -> {
                    invocation.getArgument(0);
                    if (reservation.getId() == null) reservation.setId(1L);
                    return reservation;
                });

        Reservation saved = reservationService.createReservation(reservation);

        assertEquals(1L, saved.getId());
        assertEquals(LocalDateTime.of(2025, 10,5,16,30), saved.getStartTime());
        assertEquals(LocalDateTime.of(2025,10,5,18,0), saved.getEndTime());
        assertEquals(4, saved.getParticipants());
        assertEquals("Nikolaj Albrektsen", saved.getContactName());
        assertEquals(22712123, saved.getContactPhone());
        assertEquals("nikolaja12@hotmail.com", saved.getContactEmail());
    }
}