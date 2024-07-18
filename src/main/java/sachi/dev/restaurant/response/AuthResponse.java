package sachi.dev.restaurant.response;

import lombok.Data;
import sachi.dev.restaurant.model.USER_ROLE;

@Data
public class AuthResponse {

    private String jwt;
    private String message;
    private USER_ROLE userRole;
}
