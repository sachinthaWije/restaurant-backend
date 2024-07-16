package sachi.dev.restaurant.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import sachi.dev.restaurant.model.Menu;

public interface MenuRepository extends MongoRepository<Menu, String> {
}
