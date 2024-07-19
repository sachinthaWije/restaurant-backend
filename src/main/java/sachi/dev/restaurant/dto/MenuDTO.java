package sachi.dev.restaurant.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class MenuDTO {


    private String menuId;

    @NotBlank(message = "Menu name is required")
    private String name;

    @NotBlank(message = "Menu description is required")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    private Double price;

    @NotBlank(message = "Category is required")
    private String categoryId;

    @NotBlank(message = "Restaurant is required")
    private String restaurantId;

    private Boolean isAvailable;
    private List<String> images;
    private String offerId;
}
