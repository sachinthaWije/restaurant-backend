package sachi.dev.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sachi.dev.restaurant.dto.ReservationDTO;
import sachi.dev.restaurant.dto.UserDTO;
import sachi.dev.restaurant.exception.CustomException;
import sachi.dev.restaurant.model.ReservationStatus;
import sachi.dev.restaurant.request.UpdateReservationStatusRequest;
import sachi.dev.restaurant.service.ReservationService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/staff/reservation")
public class ReservationAdminController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/{restaurantId}")
    public ResponseEntity<List<ReservationDTO>> getAllReservations(@PathVariable String restaurantId) throws Exception {
        return new ResponseEntity<>(reservationService.findReservationsByRestaurantId(restaurantId), HttpStatus.OK);
    }

    @PostMapping("/update-status/{reservationId}")
    public ResponseEntity<?> updateReservationStatus(
            @PathVariable String reservationId,
            @RequestBody UpdateReservationStatusRequest request) throws Exception {
        try {
            reservationService.updateReservationStatus(reservationId, request.getStatus());
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (CustomException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/search-reservations")
    public ResponseEntity<List<ReservationDTO>> searchReservations(
        @RequestParam(required = false) String restaurantId,
        @RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate,
        @RequestParam(required = false) String reservationType
    ){
        LocalDate start = (startDate != null) ? LocalDate.parse(startDate) : null;
        LocalDate end = (endDate != null) ? LocalDate.parse(endDate) : null;

        System.out.println("Search reservations: " + restaurantId + ", " + startDate + ", " + endDate
        + ", " + reservationType);

        return new ResponseEntity<>(reservationService.searchReservations(restaurantId, start,end, reservationType), HttpStatus.OK);
    }


}
