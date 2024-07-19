package sachi.dev.restaurant.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableDTO {
    private String tableId;

    @NotBlank(message = "Restaurant is required")
    private String restaurantId;

    @NotBlank(message = "Table Number is required")
    private String tableNumber;

    @NotBlank(message = "Capacity is required")
    private int capacity;
}
