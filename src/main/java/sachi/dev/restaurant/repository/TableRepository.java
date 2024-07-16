package sachi.dev.restaurant.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import sachi.dev.restaurant.model.Table;

public interface TableRepository extends MongoRepository<Table, String> {
}
