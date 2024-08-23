package sachi.dev.restaurant.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import sachi.dev.restaurant.dto.ReservationDTO;
import sachi.dev.restaurant.model.Reservation;
import sachi.dev.restaurant.model.ReservationStatus;
import sachi.dev.restaurant.model.Restaurant;
import sachi.dev.restaurant.repository.custom.CustomReservationRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository extends MongoRepository<Reservation, String> , CustomReservationRepository {

    List<ReservationDTO> findByCustomerId(String customerId);
    List<ReservationDTO> findByRestaurantId(String restaurantId);
    boolean existsByTableIdAndReservationDateAndReservationTime(String tableId, LocalDate date, LocalTime time);

    @Query("{ 'restaurantId':  ?0, 'reservationDate': ?1, 'reservationType':  ?2}")
    List<ReservationDTO> findByRestaurantAndDateAndType(String restaurantId, LocalDate reservationDate, String reservationType);
}
