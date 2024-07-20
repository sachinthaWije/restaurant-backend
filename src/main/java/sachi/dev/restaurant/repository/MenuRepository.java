package sachi.dev.restaurant.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import sachi.dev.restaurant.dto.MenuDTO;
import sachi.dev.restaurant.model.Menu;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends MongoRepository<Menu, String> {

    Optional<MenuDTO> findByName(String name);
}
