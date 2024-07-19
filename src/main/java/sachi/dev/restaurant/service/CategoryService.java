package sachi.dev.restaurant.service;

import sachi.dev.restaurant.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {

    CategoryDTO save(CategoryDTO category) throws Exception;
    List<CategoryDTO> findAll();
}
