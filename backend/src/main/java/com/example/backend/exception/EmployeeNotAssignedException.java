package com.example.backend.exception;

public class EmployeeNotAssignedException extends RuntimeException {
    public EmployeeNotAssignedException(String message) {
        super(message);
    }
}
