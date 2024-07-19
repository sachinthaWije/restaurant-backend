package sachi.dev.restaurant.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import sachi.dev.restaurant.dto.CategoryDTO;
import sachi.dev.restaurant.model.Category;

public interface CategoryRepository extends MongoRepository<Category, String> {

    CategoryDTO findByCategoryName(String categoryName);
}
