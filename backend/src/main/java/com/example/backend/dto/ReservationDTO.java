package com.example.backend.dto;

import java.time.LocalDateTime;

public class ReservationDTO {

    public record CreateReservationRequest(
            LocalDateTime startTime,
            LocalDateTime endTime,
            int participants,
            String contactName,
            String contactPhone,
            String contactEmail
    ) {
    }

    public record ReservationResponse(
            LocalDateTime startTime,
            LocalDateTime endTime,
            int participants,
            String contactName,
            String contactPhone,
            String contactEmail,
            String bookingCode
    ) {
    }
}