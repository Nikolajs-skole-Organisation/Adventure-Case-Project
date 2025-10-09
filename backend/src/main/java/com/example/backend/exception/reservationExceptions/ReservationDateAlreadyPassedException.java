package com.example.backend.exception.reservationExceptions;

public class ReservationDateAlreadyPassedException extends RuntimeException {
    public ReservationDateAlreadyPassedException(String message) {
        super(message);
    }
}
