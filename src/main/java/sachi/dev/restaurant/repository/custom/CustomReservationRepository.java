package sachi.dev.restaurant.repository.custom;

import sachi.dev.restaurant.dto.ReservationDTO;
import sachi.dev.restaurant.model.ReservationStatus;

public interface CustomReservationRepository {
    void updateReservationStatus(String reservationId, ReservationStatus reservationStatus);
}
