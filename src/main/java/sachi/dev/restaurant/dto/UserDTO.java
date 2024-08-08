package sachi.dev.restaurant.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sachi.dev.restaurant.model.USER_ROLE;

import java.time.LocalDateTime;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String userId;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    private String password;

    @NotBlank(message = "Role is required")
    private USER_ROLE role=USER_ROLE.ROLE_CUSTOMER;

    @NotBlank(message = "Restaurant ID is required")
    private String restaurantId;
    private String restaurantName;
    @Valid
    @NotNull(message = "Contact information is required")
    private ContactInfoDTO contactInfo;

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

