package sachi.dev.restaurant.dto;

import lombok.Data;

@Data
public class MenuSearchCriteria {

    private String restaurantId;
    private String categoryName;
    private String menuName;
    private Double minPrice;
    private Double maxPrice;
    private Boolean isAvailable;
    private String location;
}
