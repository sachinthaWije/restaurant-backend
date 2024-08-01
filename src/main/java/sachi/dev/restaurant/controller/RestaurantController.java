package sachi.dev.restaurant.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sachi.dev.restaurant.dto.RestaurantDTO;
import sachi.dev.restaurant.dto.UserDTO;
import sachi.dev.restaurant.model.Restaurant;
import sachi.dev.restaurant.model.User;
import sachi.dev.restaurant.service.RestaurantService;
import sachi.dev.restaurant.service.UserService;

@RestController
@RequestMapping
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserService userService;

    @PostMapping("/api/admin/restaurant")
    public ResponseEntity<?> createRestaurant(@Valid @RequestBody RestaurantDTO restaurantDTO,
                                              @RequestHeader("Authorization") String jwt)  throws Exception {
        UserDTO userDTO = userService.findUserByJwtToken(jwt);
        return new ResponseEntity<>(restaurantService.createRestaurant(restaurantDTO,userDTO), HttpStatus.CREATED);
    }

    @GetMapping("/restaurant")
    public ResponseEntity<?> getAllRestaurants()  throws Exception {
        return new ResponseEntity<>(restaurantService.getAllRestaurants(), HttpStatus.OK);
    }

    @GetMapping("/restaurant/{id}")
    public ResponseEntity<?> getRestaurantById(@PathVariable String id)  throws Exception {
        return new ResponseEntity<>(restaurantService.getRestaurantById(id), HttpStatus.OK);
    }

}
