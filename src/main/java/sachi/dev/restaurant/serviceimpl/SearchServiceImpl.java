package sachi.dev.restaurant.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sachi.dev.restaurant.model.Category;
import sachi.dev.restaurant.model.Menu;
import sachi.dev.restaurant.model.Restaurant;
import sachi.dev.restaurant.repository.CategoryRepository;
import sachi.dev.restaurant.repository.MenuRepository;
import sachi.dev.restaurant.repository.RestaurantRepository;
import sachi.dev.restaurant.service.SearchService;

@Service
public class SearchServiceImpl  implements SearchService {

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public Page<Menu> searchMenus(String name, Double minPrice, Double maxPrice, Pageable pageable) {
        System.out.println("name: "+name);
        String nameRegex = name != null ? ".*" + name + ".*" : ".*";
        System.out.println(menuRepository.findByNameRegexAndPriceRange(nameRegex, minPrice, maxPrice, pageable));
        return menuRepository.findByNameRegexAndPriceRange(nameRegex, minPrice, maxPrice, pageable);
    }

    @Override
    public Page<Restaurant> searchRestaurantsByLocation(String location, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Category> searchCategories(String categoryName, Pageable pageable) {
        return null;
    }
}
