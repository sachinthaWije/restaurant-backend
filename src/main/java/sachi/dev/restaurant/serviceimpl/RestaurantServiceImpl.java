package sachi.dev.restaurant.serviceimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sachi.dev.restaurant.dto.RestaurantDTO;
import sachi.dev.restaurant.dto.UserDTO;
import sachi.dev.restaurant.model.Restaurant;
import sachi.dev.restaurant.repository.RestaurantRepository;
import sachi.dev.restaurant.service.RestaurantService;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public RestaurantDTO createRestaurant(RestaurantDTO restaurantDTO , UserDTO userDTO) throws Exception {
       RestaurantDTO existingRestaurant= restaurantRepository.findByLocation(restaurantDTO.getLocation());
       if(existingRestaurant!=null){
           throw new Exception("A restaurant is already present in this location.");
       }
        Restaurant restaurant = modelMapper.map(restaurantDTO, Restaurant.class);
        restaurant.setCreatedAt(new Date());
        restaurant.setAdminId(userDTO.getUserId());
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return modelMapper.map(savedRestaurant, RestaurantDTO.class);
    }

    @Override
    public RestaurantDTO updateRestaurant(String id, RestaurantDTO restaurantDTO) {
        if (restaurantRepository.findById(id).isPresent()) {
            Restaurant restaurant = modelMapper.map(restaurantDTO, Restaurant.class);
            restaurant.setRestaurantId(id);
            Restaurant updatedRestaurant = restaurantRepository.save(restaurant);
            return modelMapper.map(updatedRestaurant, RestaurantDTO.class);
        }
        return null;
    }

    @Override
    public List<RestaurantDTO> getAllRestaurants() {
        return restaurantRepository.findAll().stream()
                .map(restaurant -> modelMapper.map(restaurant, RestaurantDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public RestaurantDTO getRestaurantById(String id) {
        return modelMapper.map(restaurantRepository.findById(id).orElse(null), RestaurantDTO.class);
    }
}
