package sachi.dev.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sachi.dev.restaurant.dto.UserDTO;
import sachi.dev.restaurant.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public ResponseEntity<UserDTO> findByJwtToken(@RequestHeader("Authorization") String jwt) throws Exception {
       return new ResponseEntity<>(userService.findUserByJwtToken(jwt), HttpStatus.OK);
    }
}
