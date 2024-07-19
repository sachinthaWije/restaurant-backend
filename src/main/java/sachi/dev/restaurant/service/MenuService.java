package sachi.dev.restaurant.service;

import sachi.dev.restaurant.dto.MenuDTO;

import java.util.List;
import java.util.Optional;

public interface MenuService {

    MenuDTO findById(String id);
    List<MenuDTO> findAll();
    MenuDTO save(MenuDTO menuDTO);
    void deleteById(String id);
    MenuDTO update(MenuDTO menuDTO,String id);
    List<MenuDTO> findByRestaurantId(String restaurantId);
}
