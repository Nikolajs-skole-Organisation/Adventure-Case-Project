package com.example.backend.dto;

import com.example.backend.model.Reservation;

import java.util.List;

public record ReservationPageResponse<T>(
        List<T> items,
        int page,
        int size,
        Long total
) {}
