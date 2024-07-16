package sachi.dev.restaurant.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import sachi.dev.restaurant.model.Query;

public interface QueryRepository extends MongoRepository<Query, String> {
}
