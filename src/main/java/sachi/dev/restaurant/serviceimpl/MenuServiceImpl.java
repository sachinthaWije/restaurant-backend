package sachi.dev.restaurant.serviceimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sachi.dev.restaurant.dto.CategoryDTO;
import sachi.dev.restaurant.dto.MenuDTO;
import sachi.dev.restaurant.exception.CustomException;
import sachi.dev.restaurant.model.Category;
import sachi.dev.restaurant.model.Menu;
import sachi.dev.restaurant.model.Restaurant;
import sachi.dev.restaurant.repository.CategoryRepository;
import sachi.dev.restaurant.repository.MenuRepository;
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
    public void addMenuToRestaurant(String restaurantId, String menuId) {
        Optional<Menu> menuOpt = menuRepository.findById(menuId);
        if (menuOpt.isPresent()) {
            Menu menu = menuOpt.get();
            Optional<Restaurant> restaurantOpt = restaurantRepository.findById(restaurantId);
            if (restaurantOpt.isPresent()) {
                Restaurant restaurant = restaurantOpt.get();
                if (restaurant.getMenuIds() != null) {
                    if (!restaurant.getMenuIds().contains(menuId)) {
                        restaurant.getMenuIds().add(menuId);
                    }else{
                        throw new CustomException("This menu already added to this restaurant", HttpStatus.CONFLICT);
                    }
                } else {
                    restaurant.setMenuIds(new ArrayList<>(List.of(menu.getMenuId())));
                }
                restaurantRepository.save(restaurant);
            }else{
                throw new CustomException("Restaurant not found with id:" + restaurantId, HttpStatus.NOT_FOUND);
            }
        }else {
            throw new CustomException("Menu not found with id:" + menuId, HttpStatus.NOT_FOUND);
        }
    }

}
