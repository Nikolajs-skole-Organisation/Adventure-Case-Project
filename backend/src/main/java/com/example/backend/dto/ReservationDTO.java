package com.example.backend.dto;

import java.time.LocalDateTime;

public class ReservationDTO {

    public record CreateReservationRequest(
            LocalDateTime startTime,
            LocalDateTime endTime,
            int participants,
            String contactName,
            String contactPhone,
            String contactEmail,
            Long activityId
    ) {
    }

    public record ReservationResponse(
            LocalDateTime startTime,
            LocalDateTime endTime,
            int participants,
            String contactName,
            String contactPhone,
            String contactEmail,
            String bookingCode,
            boolean confirmed,
            Long activityId,
            String activityName
    ) {
    }

    public record UpdateReservationRequest(
            LocalDateTime startTime,
            LocalDateTime endTime,
            Integer participants,
            String contactName,
            String contactPhone,
            String contactEmail,
            Long activityId,
            Boolean confirmed
    ){}
}