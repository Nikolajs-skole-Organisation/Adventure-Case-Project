package com.example.backend.repository;

import com.example.backend.model.Reservation;
import com.example.backend.model.ReservationSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ReservationRepositoryTest {

    @Autowired
    ReservationRepository reservationRepository;

    //inserts 3 elements into the db. Searches for all the ones named "john". Asserts that only the 2 elements
    //with the name "john" gets returned and that those 2 elements contains the correct information of the 2 johns.
    @Test
    void search_byName_returnsOnlyMatchingRows(){
        Reservation john1 = new Reservation();
        john1.setContactName("John Smith");
        john1.setStartTime(LocalDateTime.parse("2025-10-10T10:00:00"));
        john1.setEndTime(LocalDateTime.parse("2025-10-10T11:00:00"));
        john1.setParticipants(2);
        john1.setContactPhone("12345678");
        john1.setContactEmail("jsmith@example.com");
        john1.setBookingCode("BOOK00000001");
        john1.setConfirmed(false);


        Reservation john2 = new Reservation();
        john2.setContactName("John Wick");
        john2.setStartTime(LocalDateTime.parse("2025-10-11T14:00:00"));
        john2.setEndTime(LocalDateTime.parse("2025-10-11T15:00:00"));
        john2.setParticipants(4);
        john2.setContactPhone("87654321");
        john2.setContactEmail("jwick@example.com");
        john2.setBookingCode("BOOK00000002");
        john2.setConfirmed(false);

        Reservation jane = new Reservation();
        jane.setContactName("Jane Doe");
        jane.setStartTime(LocalDateTime.parse("2025-10-11T08:00:00"));
        jane.setEndTime(LocalDateTime.parse("2025-10-11T09:00:00"));
        jane.setParticipants(3);
        jane.setContactPhone("78451236");
        jane.setContactEmail("janek@example.com");
        jane.setBookingCode("BOOK00000003");
        jane.setConfirmed(false);

        reservationRepository.saveAll(List.of(john1, john2, jane));

        Pageable pageable = PageRequest.of(0, 20, Sort.by(Sort.Direction.ASC, "startTime"));
        Page<Reservation> page = reservationRepository.findAll(
                ReservationSpecification.search("john"), pageable);

        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getContent())
                .extracting(Reservation::getContactName)
                .containsExactly("John Smith", "John Wick");
        assertThat(page.getContent())
                .extracting(Reservation::getContactEmail)
                .containsExactly("jsmith@example.com", "jwick@example.com");
        assertThat(page.getContent())
                .extracting(Reservation::getContactPhone)
                .containsExactly("12345678", "87654321");
        assertThat(page.getContent())
                .extracting(Reservation::getBookingCode)
                .containsExactly("BOOK00000001", "BOOK00000002");
    }
}
