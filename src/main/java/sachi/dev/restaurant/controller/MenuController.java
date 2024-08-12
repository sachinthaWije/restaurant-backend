package sachi.dev.restaurant.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sachi.dev.restaurant.dto.CategoryDTO;
import sachi.dev.restaurant.dto.MenuDTO;
import sachi.dev.restaurant.dto.MenuSearchCriteria;
import sachi.dev.restaurant.exception.CustomException;
import sachi.dev.restaurant.model.Menu;
import sachi.dev.restaurant.response.SearchResponse;
import sachi.dev.restaurant.service.MenuService;
import sachi.dev.restaurant.service.SearchService;

import java.util.List;

@RestController
@RequestMapping
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private SearchService searchService;

    @PostMapping("/api/staff/menus")
    public ResponseEntity<MenuDTO> createMenu(@Valid @RequestBody MenuDTO menuDTO,
                                              @RequestParam String categoryId) {
        MenuDTO createdMenu = menuService.save(menuDTO, categoryId);
        return new ResponseEntity<>(createdMenu, HttpStatus.CREATED);
    }

    @GetMapping("/menus")
    public List<MenuDTO> getAllMenus() {
        return menuService.findAll();
    }

    @PutMapping("/api/staff/menus/{id}")
    public ResponseEntity<MenuDTO> updateMenu(@Valid @RequestBody MenuDTO menuDTO,
                                              @PathVariable String id) {
        return new ResponseEntity<>(menuService.update(menuDTO, id), HttpStatus.OK);
    }

    @GetMapping("/{menuId}/category")
    public ResponseEntity<CategoryDTO> getCategoryByMenuId(@PathVariable String menuId) {
        return new ResponseEntity<>(menuService.findCategoryByMenuId(menuId), HttpStatus.OK);
    }

    @PostMapping("/api/staff/add-to-restaurant/{restaurantId}")
    public ResponseEntity<?> addMenuToRestaurant(@RequestBody List<String> menuIds, @PathVariable String restaurantId) {
        try {
            menuService.addMenuToRestaurant(restaurantId, menuIds);
            return ResponseEntity.ok("Menus added to restaurant successfully.");
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @GetMapping("/menu/{id}")
    public ResponseEntity<MenuDTO> getMenuById(@PathVariable String id) {
        return new ResponseEntity<>(menuService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/api/search/menus")
    public ResponseEntity<SearchResponse> searchMenus(
            @RequestParam(required = false) String name,
            @RequestParam(required = false,defaultValue = "0") Double minPrice,
            @RequestParam(required = false,defaultValue = "1000000") Double maxPrice,
            @RequestParam(required = false) String categoryName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Menu> results = searchService.searchMenus(name, minPrice, maxPrice,categoryName, pageable);
        System.out.println("results: " + results);
        if (results.isEmpty()) {
            System.out.println("No results found for the given criteria.");
        }
        return ResponseEntity.ok(new SearchResponse(results));
    }


    @PutMapping("api/staff/{menuId}/offer/{offerId}")
    public ResponseEntity<MenuDTO> setOfferToMenu(@PathVariable String menuId, @PathVariable String offerId) {
        try {
            // Call the service method to set the offerId to the menu
            MenuDTO updatedMenu = menuService.setOfferToMenu(menuId, offerId);
            return ResponseEntity.ok(updatedMenu);
        } catch (Exception ex) {
            // Handle any other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}
