package sachi.dev.restaurant.service;

import sachi.dev.restaurant.dto.CategoryDTO;
import sachi.dev.restaurant.dto.MenuDTO;
import sachi.dev.restaurant.dto.MenuSearchCriteria;
import sachi.dev.restaurant.model.Menu;

import java.util.List;
import java.util.Optional;

public interface MenuService {

    MenuDTO findById(String id);
    List<MenuDTO> findAll();
    MenuDTO save(MenuDTO menuDTO,String categoryId);
    void deleteById(String id);
    MenuDTO update(MenuDTO menuDTO,String id);
    MenuDTO UpdateAvailability(String id);
    CategoryDTO findCategoryByMenuId(String menuId);
    void addMenuToRestaurant(String restaurantId, List<String> menuIds);
    MenuDTO setOfferToMenu(String menuId,String offerId) throws Exception;

}
