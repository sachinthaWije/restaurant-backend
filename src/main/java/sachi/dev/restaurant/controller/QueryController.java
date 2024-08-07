package sachi.dev.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sachi.dev.restaurant.dto.QueryDTO;
import sachi.dev.restaurant.dto.UserDTO;
import sachi.dev.restaurant.service.QueryService;
import sachi.dev.restaurant.service.RestaurantService;
import sachi.dev.restaurant.service.UserService;

import java.util.List;

@RestController
@RequestMapping
public class QueryController {

    @Autowired
    private QueryService queryService;

    @Autowired
    private UserService userService;

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping("/api/customer/query/{restaurantId}")
    public ResponseEntity<QueryDTO> save(@RequestBody QueryDTO queryDTO,
                                         @RequestHeader("Authorization") String jwt,
                                         @PathVariable String restaurantId) throws Exception {
        UserDTO loggedUser= userService.findUserByJwtToken(jwt);
        queryDTO.setCustomerId(loggedUser.getUserId());

        queryDTO.setRestaurantId(restaurantId);

        return new ResponseEntity<>(queryService.save(queryDTO), HttpStatus.CREATED);
    }

    @PostMapping("/api/staff/update-query/{id}")
    public ResponseEntity<QueryDTO> update(@PathVariable String id,
                                           @RequestBody QueryDTO queryDTO,
                                           @RequestHeader("Authorization") String jwt) throws Exception {
        UserDTO loggedUser= userService.findUserByJwtToken(jwt);
        queryDTO.setRespondedBy(loggedUser.getUserId());
        return new ResponseEntity<>(queryService.updateRespond(queryDTO, id), HttpStatus.OK);
    }

    @GetMapping("/api/customer/queries")
    public ResponseEntity<List<QueryDTO>> findAllByCustomerId(@RequestHeader("Authorization") String jwt) throws Exception {
          UserDTO loggedUser= userService.findUserByJwtToken(jwt);
          return new ResponseEntity<>(queryService.findAllByCustomerId(loggedUser.getUserId()), HttpStatus.OK);
    }

    @GetMapping("/api/staff/queries/{restaurantId}")
    public ResponseEntity<List<QueryDTO>> findAllByRestaurantId(@PathVariable String restaurantId) throws Exception {
        return new ResponseEntity<>(queryService.findAllByRestaurantId(restaurantId), HttpStatus.OK);
    }


}
