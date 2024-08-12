package sachi.dev.restaurant.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import sachi.dev.restaurant.dto.MenuDTO;
import sachi.dev.restaurant.model.Menu;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends MongoRepository<Menu, String> {

    Optional<MenuDTO> findByName(String name);

    @Query("{'name': {$regex: ?0, $options: 'i'}, 'price': {$gte: ?1, $lte: ?2}, 'menuId': {$in: ?3}}")
    Page<Menu> findByNameRegexAndPriceRangeAndCategoryIds(String nameRegex, Double minPrice, Double maxPrice, List<String> menuIds, Pageable pageable);
}
