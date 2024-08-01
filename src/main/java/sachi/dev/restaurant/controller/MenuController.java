package sachi.dev.restaurant.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sachi.dev.restaurant.dto.CategoryDTO;
import sachi.dev.restaurant.dto.MenuDTO;
import sachi.dev.restaurant.exception.CustomException;
import sachi.dev.restaurant.service.MenuService;

import java.util.List;

@RestController
@RequestMapping
public class MenuController {

    @Autowired
    private MenuService menuService;

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

}
