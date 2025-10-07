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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.awt.print.Pageable;
import java.time.LocalDate;
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
        verify(reservationRepository, never()).delete((Reservation) any());
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

    @Test
    void getReservationsForDate_returnsActiveSortedList() {
        LocalDate date = LocalDate.of(2025, 10, 6);
        LocalDateTime s1 = date.atTime(9, 0);
        LocalDateTime s2 = date.atTime(11, 0);

        Reservation r1 = new Reservation();
        r1.setId(1L);
        r1.setStartTime(s1);
        r1.setEndTime(s1.plusHours(1));
        r1.setParticipants(2);
        r1.setContactName("A");
        r1.setContactPhone("1");
        r1.setContactEmail("a@ex.com");
        r1.setBookingCode("CODE-1");
        r1.setConfirmed(false);

        Reservation r2 = new Reservation();
        r2.setId(2L);
        r2.setStartTime(s2);
        r2.setEndTime(s2.plusHours(1));
        r2.setParticipants(3);
        r2.setContactName("B");
        r2.setContactPhone("2");
        r2.setContactEmail("b@ex.com");
        r2.setBookingCode("CODE-2");
        r2.setConfirmed(false);

        when(reservationRepository
                .findAllByStartTimeBetweenAndConfirmedFalseOrderByStartTimeAsc(
                        date.atStartOfDay(), date.plusDays(1).atStartOfDay()))
                .thenReturn(java.util.List.of(r1, r2));

        var list = reservationService.getReservationsForDate(date);

        assertEquals(2, list.size());
        assertEquals("CODE-1", list.get(0).bookingCode());
        assertEquals("CODE-2", list.get(1).bookingCode());
        assertFalse(list.get(0).confirmed());
        assertFalse(list.get(1).confirmed());
    }


}