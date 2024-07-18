package sachi.dev.restaurant.request;

import lombok.Data;
import sachi.dev.restaurant.model.USER_ROLE;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private USER_ROLE role;
    private String restaurantId;
    private String email;
    private String phone;
}
