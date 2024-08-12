package sachi.dev.restaurant.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sachi.dev.restaurant.model.Category;
import sachi.dev.restaurant.model.Menu;
import sachi.dev.restaurant.model.Restaurant;

public interface SearchService {

    Page<Menu> searchMenus(String name, Double minPrice, Double maxPrice,String categoryName, Pageable pageable);

    public Page<Restaurant> searchRestaurantsByLocation(String location, Pageable pageable);

    public Page<Category> searchCategories(String categoryName, Pageable pageable);

}