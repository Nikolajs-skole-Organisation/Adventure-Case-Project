package com.example.backend.service;

import com.example.backend.dto.ReservationDTO;
import com.example.backend.dto.ReservationMapper;
import com.example.backend.model.BookingCodeGenerator;
import com.example.backend.model.Reservation;
import com.example.backend.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private BookingCodeGenerator codeGen;

    @Spy
    private ReservationMapper reservationMapper = new ReservationMapper();

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Test
    void createReservation_mapsAndGeneratesCode() {
        ReservationDTO.CreateReservationRequest input = new ReservationDTO.CreateReservationRequest(
                LocalDateTime.of(2025,10,5,16,30),
                LocalDateTime.of(2025,10,5,18, 0),
                4,
                "Nikolaj Albrektsen",
                "22712123",
                "nikolaja12@hotmail.com"
        );

        when(codeGen.generate()).thenReturn("RSV-TEST123");

        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(invocation -> {
                    Reservation r = invocation.getArgument(0);
                    if (r.getId() == null) r.setId(1L);
                    return r;
                });

        ReservationDTO.ReservationResponse saved = reservationService.createReservation(input);

        assertEquals("Nikolaj Albrektsen", saved.contactName());
        assertEquals(4, saved.participants());
        assertEquals(LocalDateTime.of(2025,10,5,16,30), saved.startTime());
        assertEquals(LocalDateTime.of(2025,10,5,18,0), saved.endTime());
        assertEquals("22712123", saved.contactPhone());
        assertEquals("nikolaja12@hotmail.com", saved.contactEmail());
        assertEquals("RSV-TEST123", saved.bookingCode());
    }

    @Test
    void cancelReservationByCode_deletes_whenfound() {
        String code = "M8DXC6VS2E";
        Reservation r = new Reservation();
        r.setId(123L);
        r.setBookingCode(code);

        when(reservationRepository.findByBookingCode(code)).thenReturn(Optional.of(r));

        assertDoesNotThrow(() -> reservationService.cancelReservationByCode(code));

        verify(reservationRepository).findByBookingCode(code);
        verify(reservationRepository).deleteById(123L);
        verifyNoMoreInteractions(reservationRepository);
    }

    @Test
    void cancelReservationByCode_throws_whenNothingFound(){
        String code = "NOPE";
        when(reservationRepository.findByBookingCode(code)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> reservationService.cancelReservationByCode(code));

        verify(reservationRepository).findByBookingCode(code);
        verify(reservationRepository, never()).delete(any());
    }

    @Test
    void cancelReservationByCode_throws_whenNotFound(){
        String code = "NOTFOUND";

        when(reservationRepository.findByBookingCode(code))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(IllegalArgumentException.class,
                () -> reservationService.cancelReservationByCode(code));

        assertTrue(ex.getMessage().contains("No reservation could be found with Booking code: " + code));

        verify(reservationRepository).findByBookingCode(code);
        verify(reservationRepository, never()).deleteById(any());
        verifyNoMoreInteractions(reservationRepository);
    }
}