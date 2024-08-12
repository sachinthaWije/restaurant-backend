package sachi.dev.restaurant.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import sachi.dev.restaurant.dto.CategoryDTO;
import sachi.dev.restaurant.model.Category;

import java.util.List;

public interface CategoryRepository extends MongoRepository<Category, String> {

    CategoryDTO findByCategoryName(String categoryName);
    CategoryDTO findByMenuIdsContains(String menuId);

    @Query("{'categoryName': {$regex: ?0, $options: 'i'}}")
    List<Category> findByCategoryNameRegex(String categoryNameRegex);
}
