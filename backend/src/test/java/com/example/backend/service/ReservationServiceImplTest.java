package com.example.backend.service;

import com.example.backend.dto.ReservationDTO;
import com.example.backend.dto.ReservationMapper;
import com.example.backend.exception.reservationExceptions.ReservationDateAlreadyPassedException;
import com.example.backend.exception.reservationExceptions.ReservationNotFoundException;
import com.example.backend.model.BookingCodeGenerator;
import com.example.backend.model.Reservation;
import com.example.backend.model.ReservationSpecification;
import com.example.backend.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
                "nikolaja12@hotmail.com",
                false
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
        assertFalse(saved.confirmed());
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

    //tests if the service correctly passes through the page it gets from the repository.
    //also test to see if the service actually calls repository method once and correct returns a Specification.
    @Test
    void search_withQuery_callRepoAndReturnsPage(){
        String query = "John";
        Pageable pageable = PageRequest.of(0, 20, Sort.by(Sort.Direction.ASC, "startTime"));

        Reservation r1 = new Reservation();
        r1.setId(1L);
        r1.setContactName("John Doe");
        r1.setStartTime(LocalDateTime.parse("2025-10-10T10:00:00"));
        r1.setEndTime(LocalDateTime.parse("2025-10-10T11:00:00"));
        r1.setParticipants(2);
        r1.setContactPhone("12345678");
        r1.setContactEmail("john@example.com");
        r1.setBookingCode("RSV-TEST123");

        Reservation r2 = new Reservation();
        r2.setId(2L);
        r2.setContactName("Jane Doe");
        r2.setStartTime(LocalDateTime.parse("2025-10-11T10:00:00"));
        r2.setEndTime(LocalDateTime.parse("2025-10-11T11:00:00"));
        r2.setParticipants(5);
        r2.setContactPhone("22334455");
        r2.setContactEmail("jane@example.com");
        r2.setBookingCode("RSV-TEST321");

        Page<Reservation> expected = new PageImpl<>(List.of(r1, r2), pageable, 2);
        when(reservationRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(expected);

        Page<Reservation> result = reservationService.search(query, pageable);

        assertThat(result).isSameAs(expected);

        ArgumentCaptor<Specification<Reservation>> specificationArgumentCaptor = ArgumentCaptor.forClass(Specification.class);
        verify(reservationRepository, times(1)).findAll(specificationArgumentCaptor.capture(), eq(pageable));
        assertThat(specificationArgumentCaptor.getValue()).isNotNull();
    }

    @Test
    void updateReservation_throws_reservationNotFoundException(){
        String bookingCodeToFind = "NOTFOUND";

        Reservation r1 = new Reservation();
        r1.setId(1L);
        r1.setContactName("John Doe");
        r1.setStartTime(LocalDateTime.parse("2025-10-10T10:00:00"));
        r1.setEndTime(LocalDateTime.parse("2025-10-10T11:00:00"));
        r1.setParticipants(2);
        r1.setContactPhone("12345678");
        r1.setContactEmail("john@example.com");
        r1.setBookingCode("RSV-TEST123");

        ReservationDTO.CreateReservationRequest request = new ReservationDTO.CreateReservationRequest(
                r1.getStartTime(),
                r1.getEndTime(),
                r1.getParticipants(),
                r1.getContactName(),
                r1.getContactPhone(),
                r1.getContactEmail(),
                r1.isConfirmed()
        );

        when(reservationRepository.findByBookingCode(bookingCodeToFind)).thenReturn(Optional.empty());

        ReservationNotFoundException exception = assertThrows(
                ReservationNotFoundException.class,
                () -> reservationService.updateReservation(bookingCodeToFind, request)
        );

        assertEquals("Reservation not found with booking code: " + bookingCodeToFind, exception.getMessage());
    }

    @Test
    void updateReservation_throws_reservationDateAlreadyPassedException(){
        String bookingCodeToFind = "RSV-TEST123";

        Reservation r1 = new Reservation();
        r1.setId(1L);
        r1.setContactName("John Doe");
        r1.setStartTime(LocalDateTime.parse("2025-08-08T08:00:00"));
        r1.setEndTime(LocalDateTime.parse("2025-08-08T11:00:00"));
        r1.setParticipants(2);
        r1.setContactPhone("12345678");
        r1.setContactEmail("john@example.com");
        r1.setBookingCode("RSV-TEST123");

        ReservationDTO.CreateReservationRequest request = new ReservationDTO.CreateReservationRequest(
                r1.getStartTime(),
                r1.getEndTime(),
                r1.getParticipants(),
                r1.getContactName(),
                r1.getContactPhone(),
                r1.getContactEmail(),
                r1.isConfirmed()
        );

        when(reservationRepository.findByBookingCode(bookingCodeToFind)).thenReturn(Optional.of(r1));

        ReservationDateAlreadyPassedException exception = assertThrows(
                ReservationDateAlreadyPassedException.class,
                () -> reservationService.updateReservation(bookingCodeToFind, request)
        );

        assertEquals("Cannot edit a reservation past the reservation start time", exception.getMessage());
    }

    @Test
    void updateReservation_updatesReservationCorrectly(){

    }
}