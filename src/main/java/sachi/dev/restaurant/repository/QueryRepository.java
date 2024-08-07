package sachi.dev.restaurant.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import sachi.dev.restaurant.dto.QueryDTO;
import sachi.dev.restaurant.model.Query;

import java.util.List;

public interface QueryRepository extends MongoRepository<Query, String> {
    List<QueryDTO> findAllByCustomerId(String customerId);
    List<QueryDTO> findAllByRestaurantId(String restaurantId);

}
