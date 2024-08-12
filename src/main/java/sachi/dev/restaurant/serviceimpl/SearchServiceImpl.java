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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public Page<Menu> searchMenus(String name, Double minPrice, Double maxPrice, String categoryName, Pageable pageable) {
        String nameRegex = name != null ? ".*" + name + ".*" : ".*";
        String categoryNameRegex = categoryName != null ? ".*" + categoryName + ".*" : ".*";

        List<String> categoryIds = categoryRepository.findByCategoryNameRegex(categoryNameRegex)
                .stream()
                .flatMap(category -> category.getMenuIds() != null ? category.getMenuIds().stream() : Stream.empty())
                .collect(Collectors.toList());
        System.out.println("categoryIds "+ categoryIds.size());

        return menuRepository.findByNameRegexAndPriceRangeAndCategoryIds(nameRegex, minPrice, maxPrice, categoryIds, pageable);
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
