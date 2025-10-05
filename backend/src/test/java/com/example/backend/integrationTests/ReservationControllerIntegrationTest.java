package com.example.backend.integrationTests;

import com.example.backend.controller.ReservationController;
import com.example.backend.model.Reservation;
import com.example.backend.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ReservationController.class)
public class ReservationControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;

    @Test
    void createReservation_returnCreated() throws Exception {
        Reservation mockReservation = new Reservation();
        mockReservation.setId(1L);
        mockReservation.setBookingCode("RSV-TEST123");

        when(reservationService.createReservation(any(Reservation.class)))
                .thenReturn(mockReservation);

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
                .andExpect(jsonPath("$.bookingCode").value("RSV-TEST123"));
    }
}
