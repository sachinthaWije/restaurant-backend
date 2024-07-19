package sachi.dev.restaurant.serviceimpl;

import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sachi.dev.restaurant.dto.CategoryDTO;
import sachi.dev.restaurant.exception.CustomException;
import sachi.dev.restaurant.model.Category;
import sachi.dev.restaurant.repository.CategoryRepository;
import sachi.dev.restaurant.service.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryDTO save(CategoryDTO category) throws Exception {

        CategoryDTO existingCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if (existingCategory != null) {
            throw new CustomException("Category name already exists:" + category.getCategoryName(),
                    HttpStatus.CONFLICT);
        }
        Category categoryEntity = modelMapper.map(category, Category.class);
        categoryEntity = categoryRepository.save(categoryEntity);
        return modelMapper.map(categoryEntity, CategoryDTO.class);
    }

    @Override
    public List<CategoryDTO> findAll() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());
    }
}
