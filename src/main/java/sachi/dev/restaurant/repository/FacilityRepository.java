package sachi.dev.restaurant.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import sachi.dev.restaurant.model.Facility;

public interface FacilityRepository extends MongoRepository<Facility, String> {
}
