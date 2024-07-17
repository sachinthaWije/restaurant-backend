package sachi.dev.restaurant.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import sachi.dev.restaurant.dto.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO createUser(UserDTO userDTO);
    UserDTO getUserById(String userId);
    UserDTO updateUser(String userId, UserDTO userDTO);
    List<UserDTO> getAllUsers();
    List<UserDTO> getAllUsersByRestaurantId();
    Boolean existsByUsername(String username);

}
