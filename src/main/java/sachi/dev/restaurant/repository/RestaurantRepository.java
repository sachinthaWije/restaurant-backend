package sachi.dev.restaurant.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import sachi.dev.restaurant.dto.RestaurantDTO;
import sachi.dev.restaurant.model.Restaurant;

public interface RestaurantRepository extends MongoRepository<Restaurant, String> {

    RestaurantDTO findByLocation(String location);

}
