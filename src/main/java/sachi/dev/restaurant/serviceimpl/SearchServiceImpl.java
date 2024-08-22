package sachi.dev.restaurant.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sachi.dev.restaurant.dto.MenuDTO;
import sachi.dev.restaurant.dto.OfferDTO;
import sachi.dev.restaurant.model.Category;
import sachi.dev.restaurant.model.Menu;
import sachi.dev.restaurant.model.Restaurant;
import sachi.dev.restaurant.repository.CategoryRepository;
import sachi.dev.restaurant.repository.MenuRepository;
import sachi.dev.restaurant.repository.OfferRepository;
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
    @Autowired
    private OfferRepository offerRepository;


    @Override
    public Page<MenuDTO> searchMenus(String name, Double minPrice, Double maxPrice, String categoryName, Pageable pageable) {
        String nameRegex = name != null ? ".*" + name + ".*" : ".*";
        String categoryNameRegex = categoryName != null ? ".*" + categoryName + ".*" : ".*";

        List<String> categoryIds = categoryRepository.findByCategoryNameRegex(categoryNameRegex)
                .stream()
                .flatMap(category -> category.getMenuIds() != null ? category.getMenuIds().stream() : Stream.empty())
                .collect(Collectors.toList());
        System.out.println("categoryIds "+ categoryIds.size());

        Page<Menu> menuPage= menuRepository.findByNameRegexAndPriceRangeAndCategoryIds(nameRegex, minPrice, maxPrice, categoryIds, pageable);
        List<Menu> menuList = menuPage.getContent();

        List<MenuDTO> menuDTOList = menuList.stream().map(menu -> {
            // Fetch associated OfferDTO
            OfferDTO offerDTO = offerRepository.findOfferByOfferId(menu.getOfferId());

            // Create and populate MenuDTO
            MenuDTO menuDTO = new MenuDTO();
            menuDTO.setName(menu.getName());
            menuDTO.setPrice(menu.getPrice());
            menuDTO.setDescription(menu.getDescription());
            menuDTO.setMenuId(menu.getMenuId());
            menuDTO.setIsAvailable(menu.getIsAvailable());
            menuDTO.setImages(menu.getImages());
            if (offerDTO != null) {  // Ensure offerDTO is not null to avoid NullPointerException
                menuDTO.setOfferName(offerDTO.getName() + " - " + offerDTO.getDiscountPercentage() + "%");
                menuDTO.setOfferId(offerDTO.getOfferId());
                menuDTO.setDiscountPercentage(offerDTO.getDiscountPercentage());
            }

            return menuDTO;
        }).collect(Collectors.toList());

        Page<MenuDTO> menuDTOPage = new PageImpl<>(menuDTOList, pageable, menuPage.getTotalElements());
        return menuDTOPage;
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
