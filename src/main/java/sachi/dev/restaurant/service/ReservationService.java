package sachi.dev.restaurant.service;

import org.springframework.cglib.core.Local;
import sachi.dev.restaurant.dto.ReservationDTO;
import sachi.dev.restaurant.model.ReservationStatus;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {
    ReservationDTO create(ReservationDTO reservationDTO);

    ReservationDTO findByReservationId(String reservationId);

    List<ReservationDTO> findReservationsByCustomerId(String customerId);

    List<ReservationDTO> findReservationsByRestaurantId(String restaurantId);

    ReservationDTO update(String reservationId, ReservationDTO reservationDTO);

    void updateReservationStatus(String reservationId, ReservationStatus reservationStatus);

    List<ReservationDTO> searchReservations(String restaurantId, LocalDate startDate,LocalDate endDate,String reservationType);
}
