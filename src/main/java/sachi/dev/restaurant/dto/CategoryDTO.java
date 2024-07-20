package sachi.dev.restaurant.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {

    private String categoryId;

    @NotBlank(message = "Category Name is required")
    private String categoryName;

    private List<String> menuIds;
}
