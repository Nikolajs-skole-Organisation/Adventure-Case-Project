package com.example.backend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation",
        indexes = @Index(name = "ux_reservation_booking_code", columnList = "bookingCode", unique = true))
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int participants;
    private String contactName;
    private String contactPhone;
    private String contactEmail;

    @Column(nullable = false, unique = true, length = 12)
    private String bookingCode;

    public Reservation(Long id, LocalDateTime startTime, LocalDateTime endTime, int participants, String contactName, String contactPhone, String contactEmail) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.participants = participants;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
    }

    public Reservation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }
}
