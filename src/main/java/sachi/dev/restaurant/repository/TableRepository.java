package sachi.dev.restaurant.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import sachi.dev.restaurant.dto.TableDTO;
import sachi.dev.restaurant.model.Table;

import java.util.List;

public interface TableRepository extends MongoRepository<Table, String> {

    List<TableDTO> findTablesByRestaurantId(String restaurantId);
}
