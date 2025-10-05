package com.example.backend.dto;

import java.time.LocalDateTime;

public record ReservationDTO(LocalDateTime startTime, LocalDateTime endTime, int participants, String contactName, int contactPhone, String contactEmail) {
}
