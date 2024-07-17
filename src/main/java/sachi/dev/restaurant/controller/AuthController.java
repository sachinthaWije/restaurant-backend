package sachi.dev.restaurant.controller;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sachi.dev.restaurant.config.JwtProvider;
import sachi.dev.restaurant.dto.UserDTO;
import sachi.dev.restaurant.model.User;
import sachi.dev.restaurant.request.LoginRequest;
import sachi.dev.restaurant.request.RegisterRequest;
import sachi.dev.restaurant.response.AuthResponse;
import sachi.dev.restaurant.service.CustomerUserDetailsService;
import sachi.dev.restaurant.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private CustomerUserDetailsService customerUserDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> signInHandler(@RequestBody LoginRequest req) throws Exception {

        String username = req.getUsername();
        String password = req.getPassword();
        Authentication auth = authenticate(username, password);

        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        String role = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();

        String jwt = jwtProvider.generateToken(auth);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Login successful");
        authResponse.setUserRole(role);

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    private Authentication authenticate(String username, String password) {

        UserDetails userDetails = customerUserDetailsService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username or password");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @PostMapping("/register/customer")
    public ResponseEntity<?> registerCustomer(@RequestBody RegisterRequest registerRequest) {
        System.out.println("registerCustomer");
        if (userService.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        UserDTO userDTO = getUserDTO(registerRequest);

        User user = modelMapper.map(userService.createUser(userDTO),User.class);

        Authentication auth = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = jwtProvider.generateToken(auth);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Register successful");
        authResponse.setUserRole(user.getRole());

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/register/staff")
    public ResponseEntity<?> registerStaff(@RequestBody RegisterRequest registerRequest) {
        if (userService.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }
        if (registerRequest.getRestaurantId() == null || registerRequest.getRestaurantId().isEmpty()) {
            return ResponseEntity.badRequest().body("Error: Restaurant ID is required for staff registration!");
        }

        UserDTO userDTO = getUserDTO(registerRequest);

        User user = modelMapper.map(userService.createUser(userDTO),User.class);

        Authentication auth = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = jwtProvider.generateToken(auth);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Register successful");
        authResponse.setUserRole(user.getRole());

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }




    private static UserDTO getUserDTO(RegisterRequest registerRequest) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(registerRequest.getUsername());
        userDTO.setPassword(registerRequest.getPassword());
        userDTO.setRole(registerRequest.getRole());
        userDTO.setRestaurantId(registerRequest.getRestaurantId());
        UserDTO.ContactInfoDTO contactInfo =new UserDTO.ContactInfoDTO();
        contactInfo.setEmail(registerRequest.getEmail());
        contactInfo.setPhone(registerRequest.getPhone());

        userDTO.setContactInfo(contactInfo);
        return userDTO;
    }
}
