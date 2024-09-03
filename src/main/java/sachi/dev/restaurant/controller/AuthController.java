package sachi.dev.restaurant.controller;


import org.checkerframework.checker.units.qual.A;
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
import org.springframework.web.bind.annotation.*;
import sachi.dev.restaurant.config.JwtProvider;
import sachi.dev.restaurant.dto.UserDTO;
import sachi.dev.restaurant.model.USER_ROLE;
import sachi.dev.restaurant.model.User;
import sachi.dev.restaurant.request.LoginRequest;
import sachi.dev.restaurant.request.RegisterRequest;
import sachi.dev.restaurant.response.AuthResponse;
import sachi.dev.restaurant.service.CustomerUserDetailsService;
import sachi.dev.restaurant.service.EmailService;
import sachi.dev.restaurant.service.UserService;

import java.io.IOException;
import java.security.SecureRandom;
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

    @Autowired
    private EmailService emailService;

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
        authResponse.setUserRole(USER_ROLE.valueOf(role));
        authResponse.setName(username);

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

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterRequest registerRequest) {
        if (userService.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        UserDTO userDTO = getUserDTO(registerRequest);

        User user = modelMapper.map(userService.createUser(userDTO), User.class);

        Authentication auth = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = jwtProvider.generateToken(auth);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Register successful");
        authResponse.setUserRole(user.getRole());

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/register/customer")
    public ResponseEntity<?> registerCustomer(@RequestBody RegisterRequest registerRequest) throws IOException {
        System.out.println("registerCustomer");
        if (userService.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        UserDTO userDTO = getUserDTO(registerRequest);

        User user = modelMapper.map(userService.createUser(userDTO), User.class);

        Authentication auth = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = jwtProvider.generateToken(auth);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Register successful");
        authResponse.setUserRole(user.getRole());

        emailService.sendRegistrationEmail(userDTO.getContactInfo().getEmail(),
                "Registration Confirmation",
                customerRegistrationEmailContent(userDTO.getUsername()));

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/register/staff/{restaurantId}")
    public ResponseEntity<?> registerStaff(@RequestBody RegisterRequest registerRequest,
                                           @PathVariable String restaurantId) throws IOException {
        if (userService.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }
        if (restaurantId == null || restaurantId.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: Restaurant ID is required for staff registration!");
        }
        registerRequest.setRestaurantId(restaurantId);
        String generatedPassword = generateRandomPassword(10); // Password length is 10 characters
        System.out.println("generatedPassword " + generatedPassword);
        registerRequest.setPassword(generatedPassword);

        UserDTO userDTO = getUserDTO(registerRequest);

        User user = modelMapper.map(userService.createUser(userDTO), User.class);

        Authentication auth = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = jwtProvider.generateToken(auth);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Register successful");
        authResponse.setUserRole(user.getRole());

        emailService.sendRegistrationEmail(userDTO.getContactInfo().getEmail(),
                "Registration Confirmation",
                staffRegistrationEmailContent(userDTO.getUsername(),userDTO.getUsername(),userDTO.getPassword()));

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }


    private static UserDTO getUserDTO(RegisterRequest registerRequest) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(registerRequest.getUsername());
        userDTO.setPassword(registerRequest.getPassword());
        userDTO.setRole(registerRequest.getRole());
        userDTO.setRestaurantId(registerRequest.getRestaurantId());
        UserDTO.ContactInfoDTO contactInfo = new UserDTO.ContactInfoDTO();
        contactInfo.setEmail(registerRequest.getEmail());
        contactInfo.setPhone(registerRequest.getPhone());

        userDTO.setContactInfo(contactInfo);
        return userDTO;
    }

    private String generateRandomPassword(int length) {
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specialChars = "!@#$%^&*()-_=+<>?";
        String combinedChars = upperCaseLetters + lowerCaseLetters + digits + specialChars;

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        // Ensure at least one character from each group
        password.append(upperCaseLetters.charAt(random.nextInt(upperCaseLetters.length())));
        password.append(lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(specialChars.charAt(random.nextInt(specialChars.length())));

        // Fill remaining characters randomly
        for (int i = 4; i < length; i++) {
            password.append(combinedChars.charAt(random.nextInt(combinedChars.length())));
        }

        // Shuffle the characters to ensure randomness
        return password.toString();
    }

    public String customerRegistrationEmailContent(String name) {
        String htmlContent = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Registration Confirmation</title>\n" +
                "</head>\n" +
                "<body style=\"font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px;\">\n" +
                "    <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"background-color: #f8f8f8; border-radius: 5px;\">\n" +
                "        <tr>\n" +
                "            <td style=\"padding: 20px;\">\n" +
                "                <h1 style=\"color: #4a4a4a; text-align: center; margin-bottom: 20px;\">Registration Confirmation</h1>\n" +
                "                <p style=\"font-size: 16px; margin-bottom: 20px;\">Dear {{name}},</p>\n" +
                "                <p style=\"font-size: 16px; margin-bottom: 20px;\">Thank you for registering with our service! We're excited to have you on board.</p>\n" +
                "                <div style=\"background-color: #ffffff; border-radius: 5px; padding: 20px; margin-bottom: 20px;\">\n" +
                "                    <h2 style=\"color: #4a4a4a; margin-top: 0;\">Welcome to ABC Restaurant</h2>\n" +
                "                    <p style=\"font-size: 16px;\">We look forward to serving you delicious meals and providing an exceptional dining experience.</p>\n" +
                "                </div>\n" +
                "                <p style=\"font-size: 16px; margin-bottom: 20px;\">If you have any questions or need assistance, please don't hesitate to contact us.</p>\n" +
                "                <p style=\"font-size: 16px; margin-bottom: 20px;\">Best regards,<br>The ABC Restaurant Team</p>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td style=\"background-color: #4a4a4a; color: #ffffff; text-align: center; padding: 10px; font-size: 14px;\">\n" +
                "                © 2024 ABC Restaurant. All rights reserved.\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "</body>\n" +
                "</html>";
        htmlContent = htmlContent.replace("{{name}}", name);
        return htmlContent;
    }

    private String staffRegistrationEmailContent(String name, String username, String password) {
        String htmlContent = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Registration Confirmation</title>\n" +
                "</head>\n" +
                "<body style=\"font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px;\">\n" +
                "    <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"background-color: #f8f8f8; border-radius: 5px;\">\n" +
                "        <tr>\n" +
                "            <td style=\"padding: 20px;\">\n" +
                "                <h1 style=\"color: #4a4a4a; text-align: center; margin-bottom: 20px;\">Registration Confirmation</h1>\n" +
                "                <p style=\"font-size: 16px; margin-bottom: 20px;\">Dear {{name}},</p>\n" +
                "                <p style=\"font-size: 16px; margin-bottom: 20px;\">Thank you for registering with our service! We're excited to have you on board.</p>\n" +
                "                <div style=\"background-color: #ffffff; border-radius: 5px; padding: 20px; margin-bottom: 20px;\">\n" +
                "                    <h2 style=\"color: #4a4a4a; margin-top: 0;\">Welcome to ABC Restaurant</h2>\n" +
                "                    <p style=\"font-size: 16px;\">We look forward to serving you delicious meals and providing an exceptional dining experience.</p>\n" +
                "                    <p style=\"font-size: 16px; margin-top: 20px;\">Your account details are as follows:</p>\n" +
                "                    <p style=\"font-size: 16px;\">Username: <strong>{{username}}</strong></p>\n" +
                "                    <p style=\"font-size: 16px;\">Password: <strong>{{password}}</strong></p>\n" +
                "                </div>\n" +
                "                <p style=\"font-size: 16px; margin-bottom: 20px;\">If you have any questions or need assistance, please don't hesitate to contact us.</p>\n" +
                "                <p style=\"font-size: 16px; margin-bottom: 20px;\">Best regards,<br>The ABC Restaurant Team</p>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td style=\"background-color: #4a4a4a; color: #ffffff; text-align: center; padding: 10px; font-size: 14px;\">\n" +
                "                © 2024 ABC Restaurant. All rights reserved.\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "</body>\n" +
                "</html>";

        // Replace placeholders with actual values
        htmlContent = htmlContent.replace("{{name}}", name);
        htmlContent = htmlContent.replace("{{username}}", username);
        htmlContent = htmlContent.replace("{{password}}", password);

        return htmlContent;
    }
}
