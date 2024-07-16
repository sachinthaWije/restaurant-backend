package sachi.dev.restaurant.service;

import sachi.dev.restaurant.dto.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO createUser(UserDTO userDTO);
    UserDTO getUserById(String userId);
    UserDTO getUserByUsername(String username);
    UserDTO updateUser(String userId, UserDTO userDTO);
    List<UserDTO> getAllUsers();
    List<UserDTO> getAllUsersByRestaurantId();
}
