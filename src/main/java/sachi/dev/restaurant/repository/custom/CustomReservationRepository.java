package sachi.dev.restaurant.repository.custom;

import sachi.dev.restaurant.dto.ReservationDTO;
import sachi.dev.restaurant.model.ReservationStatus;

import java.time.LocalDate;
import java.util.List;

public interface CustomReservationRepository {
    void updateReservationStatus(String reservationId, ReservationStatus reservationStatus);

    List<ReservationDTO> findByCriteria(String restaurantId, LocalDate startDate, LocalDate endDate, String reservationType);

}
