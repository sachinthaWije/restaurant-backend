package sachi.dev.restaurant.serviceimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sachi.dev.restaurant.dto.MenuDTO;
import sachi.dev.restaurant.exception.CustomException;
import sachi.dev.restaurant.model.Menu;
import sachi.dev.restaurant.repository.MenuRepository;
import sachi.dev.restaurant.repository.RestaurantRepository;
import sachi.dev.restaurant.service.MenuService;

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
    public MenuDTO save(MenuDTO menuDTO) {
        Optional<MenuDTO> existingMenu = menuRepository.findByNameAndCategoryId(menuDTO.getName(),
                menuDTO.getCategoryId());
        if (existingMenu.isPresent()) {
            throw new CustomException("Menu already exists in this category", HttpStatus.CONFLICT);
        }

        Menu menu = modelMapper.map(menuDTO, Menu.class);
        menu = menuRepository.save(menu);
        return modelMapper.map(menu, MenuDTO.class);
    }

    @Override
    public void deleteById(String id) {

    }

    @Override
    public MenuDTO update(MenuDTO menuDTO, String id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new CustomException("Menu item not found", HttpStatus.NOT_FOUND));

        Optional<MenuDTO> existingMenu = menuRepository.findByNameAndCategoryId(menuDTO.getName(),
                menuDTO.getCategoryId());
        if (existingMenu.isPresent() && !existingMenu.get().getMenuId().equals(id)) {
            throw new CustomException("Menu name already exists in this category: " + menuDTO.getName(),
                    HttpStatus.CONFLICT);
        }

        menu.setName(menuDTO.getName());
        menu.setDescription(menuDTO.getDescription());
        menu.setPrice(menuDTO.getPrice());
        menu.setCategoryId(menuDTO.getCategoryId());
        menu.setRestaurantId(menuDTO.getRestaurantId());

        menu = menuRepository.save(menu);
        return modelMapper.map(menu, MenuDTO.class);
    }

    @Override
    public List<MenuDTO> findByRestaurantId(String restaurantId) {
        return menuRepository.findByRestaurantId(restaurantId);
    }
}
