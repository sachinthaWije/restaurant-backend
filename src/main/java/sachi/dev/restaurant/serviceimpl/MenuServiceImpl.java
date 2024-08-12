package sachi.dev.restaurant.serviceimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sachi.dev.restaurant.dto.CategoryDTO;
import sachi.dev.restaurant.dto.MenuDTO;
import sachi.dev.restaurant.dto.MenuSearchCriteria;
import sachi.dev.restaurant.dto.OfferDTO;
import sachi.dev.restaurant.exception.CustomException;
import sachi.dev.restaurant.model.Category;
import sachi.dev.restaurant.model.Menu;
import sachi.dev.restaurant.model.Restaurant;
import sachi.dev.restaurant.repository.CategoryRepository;
import sachi.dev.restaurant.repository.MenuRepository;
import sachi.dev.restaurant.repository.OfferRepository;
import sachi.dev.restaurant.repository.RestaurantRepository;
import sachi.dev.restaurant.service.MenuService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OfferRepository offerRepository;

    @Override
    public MenuDTO findById(String id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new CustomException("Menu item not found", HttpStatus.NOT_FOUND));
        return modelMapper.map(menu, MenuDTO.class);
    }

    @Override
    public List<MenuDTO> findAll() {
        return menuRepository.findAll().stream()
                .map(menu -> modelMapper.map(menu, MenuDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public MenuDTO save(MenuDTO menuDTO, String categoryId) {
        Optional<MenuDTO> existingMenu = menuRepository.findByName(menuDTO.getName());
        if (existingMenu.isPresent()) {
            throw new CustomException("Menu already exists in this category", HttpStatus.CONFLICT);
        }

        Menu menu = modelMapper.map(menuDTO, Menu.class);
        menu = menuRepository.save(menu);

        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
        if (categoryOpt.isPresent()) {
            Category category = categoryOpt.get();
            if (category.getMenuIds() != null) {
                category.getMenuIds().add(menu.getMenuId());
            } else {
                category.setMenuIds(new ArrayList<>(List.of(menu.getMenuId())));
            }
            categoryRepository.save(category);
        }

        return modelMapper.map(menu, MenuDTO.class);
    }

    @Override
    public void deleteById(String id) {

    }

    @Override
    public MenuDTO update(MenuDTO menuDTO, String id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new CustomException("Menu item not found", HttpStatus.NOT_FOUND));

        Optional<MenuDTO> existingMenu = menuRepository.findByName(menuDTO.getName());
        if (existingMenu.isPresent() && !existingMenu.get().getMenuId().equals(id)) {
            throw new CustomException("Menu name already exists in this category: " + menuDTO.getName(),
                    HttpStatus.CONFLICT);
        }

        menu.setName(menuDTO.getName());
        menu.setDescription(menuDTO.getDescription());
        menu.setPrice(menuDTO.getPrice());
        menu.setImages(menuDTO.getImages());

        menu = menuRepository.save(menu);
        return modelMapper.map(menu, MenuDTO.class);
    }

    @Override
    public MenuDTO UpdateAvailability(String id) {
        Optional<Menu> opt = menuRepository.findById(id);
        if (opt.isPresent()) {
            Menu menu = opt.get();
            menu.setIsAvailable(!menu.getIsAvailable());
            menu = menuRepository.save(menu);
            return modelMapper.map(menu, MenuDTO.class);
        } else {
            throw new CustomException("Menu not found with id:" + id, HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public CategoryDTO findCategoryByMenuId(String menuId) {
        return categoryRepository.findByMenuIdsContains(menuId);
    }

    @Override
    public void addMenuToRestaurant(String restaurantId, List<String> menuIds) {
        List<Menu> menus = new ArrayList<>();
        for (String menuId : menuIds) {
            Optional<Menu> menuOpt = menuRepository.findById(menuId);
            if (menuOpt.isPresent()) {
                menus.add(menuOpt.get());
            } else {
                throw new CustomException("Menu not found with id:" + menuId, HttpStatus.NOT_FOUND);
            }
        }


        Optional<Restaurant> restaurantOpt = restaurantRepository.findById(restaurantId);
        if (restaurantOpt.isPresent()) {
            Restaurant restaurant = restaurantOpt.get();
            if (restaurant.getMenuIds() == null) {
                restaurant.setMenuIds(new ArrayList<>());
            }

            for (Menu menu : menus) {
                if (!restaurant.getMenuIds().contains(menu.getMenuId())) {
                    restaurant.getMenuIds().add(menu.getMenuId());
                } else {
                    throw new CustomException("Menu with id " + menu.getMenuId() + " already added to this restaurant", HttpStatus.CONFLICT);
                }
            }
            restaurantRepository.save(restaurant);
        } else {
            throw new CustomException("Restaurant not found with id:" + restaurantId, HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public MenuDTO setOfferToMenu(String menuId, String offerId) throws Exception {
        Optional<Menu> menu = menuRepository.findById(menuId);

        if (menu.isPresent()) {
            // Find the offer by its ID to ensure it exists
            OfferDTO offer = offerRepository.findOfferByOfferId(offerId);

            if (offer != null) {
                // Set the offerId to the menu
                menu.get().setOfferId(offerId);

                // Save the updated menu back to the repository
                Menu updatedMenu = menuRepository.save(menu.get());
                return modelMapper.map(updatedMenu, MenuDTO.class);
            } else {
                // Handle the case where the offer does not exist
                throw new Exception("Offer with ID " + offerId + " not found.");
            }
        } else {
            // Handle the case where the menu does not exist
            throw new Exception("Menu with ID " + menuId + " not found.");
        }
    }

}
