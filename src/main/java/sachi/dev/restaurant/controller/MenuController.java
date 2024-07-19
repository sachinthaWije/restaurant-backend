package sachi.dev.restaurant.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sachi.dev.restaurant.dto.MenuDTO;
import sachi.dev.restaurant.service.MenuService;

import java.util.List;

@RestController
@RequestMapping
public class MenuController {

    @Autowired
    private MenuService menuService;

    @PostMapping("/api/staff/menus")
    public ResponseEntity<MenuDTO> createMenu(@Valid @RequestBody MenuDTO menuDTO) {
        MenuDTO createdMenu=menuService.save(menuDTO);
        return new ResponseEntity<>(createdMenu, HttpStatus.CREATED);
    }

    @GetMapping("/menus")
    public List<MenuDTO> getAllMenus() {
        return menuService.findAll();
    }
}
