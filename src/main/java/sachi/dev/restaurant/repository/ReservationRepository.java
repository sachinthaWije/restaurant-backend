package sachi.dev.restaurant.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import sachi.dev.restaurant.model.Reservation;

public interface ReservationRepository extends MongoRepository<Reservation, String> {
}
