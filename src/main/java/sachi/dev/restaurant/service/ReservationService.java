package sachi.dev.restaurant.service;

import sachi.dev.restaurant.dto.ReservationDTO;
import sachi.dev.restaurant.model.ReservationStatus;

import java.util.List;

public interface ReservationService {
    ReservationDTO create(ReservationDTO reservationDTO);
    ReservationDTO findByReservationId(String reservationId);
    List<ReservationDTO> findReservationsByCustomerId(String customerId);
    List<ReservationDTO> findReservationsByRestaurantId(String restaurantId);
    ReservationDTO update(String reservationId, ReservationDTO reservationDTO);
    void updateReservationStatus(String reservationId, ReservationStatus reservationStatus);
}
