package com.example.backend.model;

import org.springframework.data.jpa.domain.Specification;

public final class ReservationSpecification {
    private ReservationSpecification() {
    }

    public static Specification<Reservation> search(String raw) {
        if (raw == null || raw.isBlank()) return ((root, query, criteriaBuilder)
                -> criteriaBuilder.conjunction());

        String term = "%" + raw.toLowerCase() + "%";
        return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("contactName")), term),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("contactEmail")), term),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("contactPhone")), term),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("bookingCode")), term)
        );
    }
}
