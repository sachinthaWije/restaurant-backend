package sachi.dev.restaurant.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import sachi.dev.restaurant.model.Restaurant;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDTO {

    private String restaurantId;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Admin is required")
    private String adminId;

    @Valid
    @NotNull(message = "Contact information is required")
    private Restaurant.ContactInfo contactInfo;


    private List<String> photoGallery;
    private List<String> facilities;

    private Date createdAt;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ContactInfoDTO {
        @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number")
        private String phone;

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        private String email;
    }
}
