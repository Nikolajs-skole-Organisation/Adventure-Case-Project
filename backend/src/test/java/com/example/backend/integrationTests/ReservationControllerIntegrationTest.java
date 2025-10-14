package com.example.backend.integrationTests;

import com.example.backend.controller.ReservationController;
import com.example.backend.dto.ReservationDTO;
import com.example.backend.model.Activity;
import com.example.backend.model.Reservation;
import com.example.backend.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@WebMvcTest(controllers = ReservationController.class)
public class ReservationControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;

    @Test
    void createReservation_returnCreated() throws Exception {

        Activity a1 = new Activity(1L, "Gokart", "Fun", 300, 10, 100, 6);

        ReservationDTO.ReservationResponse returned =
                new ReservationDTO.ReservationResponse(
                        LocalDateTime.parse("2025-10-05T16:30:00"),
                        LocalDateTime.parse("2025-10-05T18:00:00"),
                        4,
                        "John Doe",
                        "12345678",
                        "john@example.com",
                        "RSV-TEST123",
                        false,
                        a1.getId(),
                        a1.getName()
                );

        when(reservationService.createReservation(any(ReservationDTO.CreateReservationRequest.class)))
                .thenReturn(returned);

        String body = """
                {
                    "startTime": "2025-10-05T16:30:00",
                    "endTime": "2025-10-05T18:00:00",
                    "participants": 4,
                    "contactName": "John Doe",
                    "contactPhone": "12345678",
                    "contactEmail": "john@example.com"
                }
                """;

        mockMvc.perform(post("/api/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.contactName").value("John Doe"))
                .andExpect(jsonPath("$.participants").value(4))
                .andExpect(jsonPath("$.bookingCode").value("RSV-TEST123"));
    }

    @Test
    void listForSpecificDate_returnsList_whenExists() throws Exception {


        Activity a1 = new Activity(1L, "Gokart", "Fun", 300, 10, 100, 6);

        var item = new ReservationDTO.ReservationResponse(
                LocalDateTime.parse("2025-10-06T09:00:00"),
                LocalDateTime.parse("2025-10-06T10:00:00"),
                2,
                "Anna",
                "11111111",
                "anna@example.com",
                "CODE-1",
                false,
                a1.getId(),
                a1.getName()
        );
        when(reservationService.getReservationsForDate(LocalDate.parse("2025-10-06")))
                .thenReturn(java.util.List.of(item));

        mockMvc.perform(get("/api/reservation?date=2025-10-06"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookingCode").value("CODE-1"))
                .andExpect(jsonPath("$[0].contactName").value("Anna"));
    }
}
