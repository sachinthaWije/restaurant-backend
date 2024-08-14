package sachi.dev.restaurant.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @Min(1)
    @Max(10)
    private Integer capacity;
}
