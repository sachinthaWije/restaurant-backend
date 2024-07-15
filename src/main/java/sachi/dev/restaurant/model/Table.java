package sachi.dev.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tables")
public class Table {

    @Id
    private String tableId;
    private String restaurantId;
    private String tableNumber;
    private int capacity;
}
