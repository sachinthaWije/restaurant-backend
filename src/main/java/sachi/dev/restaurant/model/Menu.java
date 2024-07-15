package sachi.dev.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "menus")
public class Menu {

    @Id
    private String menuId;
    private String name;
    private String description;
    private Double price;
    private String categoryId;
    private String restaurantId;
    private Boolean isAvailable;
    private List<String> images;
    private String offerId;
}
