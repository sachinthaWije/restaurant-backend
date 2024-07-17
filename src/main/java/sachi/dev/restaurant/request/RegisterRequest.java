package sachi.dev.restaurant.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String role;
    private String restaurantId;
    private String email;
    private String phone;
}
