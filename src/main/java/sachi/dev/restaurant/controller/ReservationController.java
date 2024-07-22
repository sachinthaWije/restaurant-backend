package sachi.dev.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sachi.dev.restaurant.dto.ReservationDTO;
import sachi.dev.restaurant.dto.UserDTO;
import sachi.dev.restaurant.exception.CustomException;
import sachi.dev.restaurant.service.ReservationService;
import sachi.dev.restaurant.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/customer/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    UserService userService;

    @PostMapping()
    public ResponseEntity<?> createReservation(@RequestBody ReservationDTO reservationDTO,
                                               @RequestHeader("Authorization") String jwt) throws Exception {
        try {
            UserDTO userDTO = userService.findUserByJwtToken(jwt);
            reservationDTO.setCustomerId(userDTO.getUserId());
            return new ResponseEntity<>(reservationService.create(reservationDTO), HttpStatus.CREATED);
        } catch (CustomException e) {
            return new ResponseEntity<>(e.getMessage(), e.getStatus());
        }
    }

    @GetMapping()
    public ResponseEntity<List<ReservationDTO>> getAllReservations(@RequestHeader("Authorization") String jwt) throws Exception {
        UserDTO userDTO = userService.findUserByJwtToken(jwt);
        return new ResponseEntity<>(reservationService.findReservationsByCustomerId(userDTO.getUserId()), HttpStatus.OK);
    }


}
