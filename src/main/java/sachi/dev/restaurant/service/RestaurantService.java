package sachi.dev.restaurant.service;

import sachi.dev.restaurant.dto.RestaurantDTO;
import sachi.dev.restaurant.dto.UserDTO;

import java.util.List;

public interface RestaurantService {

    RestaurantDTO createRestaurant(RestaurantDTO restaurantDTO, UserDTO userDTO) throws Exception;
    RestaurantDTO updateRestaurant(String id,RestaurantDTO restaurantDTO);
    List<RestaurantDTO> getAllRestaurants();
    RestaurantDTO getRestaurantById(String id);
}
