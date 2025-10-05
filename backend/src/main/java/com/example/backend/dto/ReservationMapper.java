package com.example.backend.dto;

import com.example.backend.model.Reservation;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    public ReservationDTO toDto(Reservation reservation){
        if (reservation == null) return null;
        return new ReservationDTO(reservation.getStartTime(), reservation.getEndTime(), reservation.getParticipants(),
                reservation.getContactName(), reservation.getContactPhone(), reservation.getContactEmail());
    }

    public Reservation toEntity(ReservationDTO reservationDto) {
        if (reservationDto == null) return null;
        Reservation r = new Reservation();
        r.setStartTime(reservationDto.startTime());
        r.setEndTime(reservationDto.endTime());
        r.setParticipants(reservationDto.participants());
        r.setContactName(reservationDto.contactName());
        r.setContactPhone(reservationDto.contactPhone());
        r.setContactEmail(reservationDto.contactEmail());

        return r;
    }
}
