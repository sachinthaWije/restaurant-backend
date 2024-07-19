package sachi.dev.restaurant.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sachi.dev.restaurant.dto.CategoryDTO;
import sachi.dev.restaurant.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/api/staff/category")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody  CategoryDTO categoryDTO) throws Exception {
       CategoryDTO savedCategory= categoryService.save(categoryDTO);
       return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }

    @GetMapping("/category")
    public List<CategoryDTO> findAll() {
        return categoryService.findAll();
    }
}
