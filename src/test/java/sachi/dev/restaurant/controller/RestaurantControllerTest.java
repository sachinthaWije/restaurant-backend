package sachi.dev.restaurant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sachi.dev.restaurant.dto.RestaurantDTO;
import sachi.dev.restaurant.dto.UserDTO;
import sachi.dev.restaurant.model.Restaurant;
import sachi.dev.restaurant.service.RestaurantService;
import sachi.dev.restaurant.service.UserService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RestaurantControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestaurantService restaurantService;

    @MockBean
    private UserService userService;

    private RestaurantDTO restaurantDTO;
    private UserDTO userDTO;
    private String validToken = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE3MjUwNDI2NzAsImV4cCI6MTcyNTEyOTA3MCwiZW1haWwiOiJhZG1pbiIsImF1dGhvcml0aWVzIjoiUk9MRV9BRE1JTiJ9.XywuITxO89hyJ9q0MNhVFwS-gHd8B0jS_msaVrANUygvAZJhrxL3Cn3ha-NFhck7fNcsyiwUO06aL3wGvbcF4A";

    @BeforeEach
    public void setUp() {
        restaurantDTO = new RestaurantDTO();
        restaurantDTO.setName("Test Restaurant");
        restaurantDTO.setLocation("Colombo");
        restaurantDTO.setAdminId("admin123");

        Restaurant.ContactInfo contactInfo = new Restaurant.ContactInfo();
        contactInfo.setEmail("admin123@gmail.com");
        contactInfo.setPhone("0771111111");

        restaurantDTO.setContactInfo(contactInfo);


        userDTO = new UserDTO();
        userDTO.setUserId("admin123");
    }

//    TC-001
    @Test
    public void createRestaurant_ValidRequest_ShouldReturnCreated() throws Exception {
        Mockito.when(userService.findUserByJwtToken(validToken)).thenReturn(userDTO);
        Mockito.when(restaurantService.createRestaurant(any(RestaurantDTO.class), eq(userDTO))).thenReturn(restaurantDTO);

        mockMvc.perform(post("/api/admin/restaurant")
                        .header("Authorization", validToken)  // Include Authorization header
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(restaurantDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Restaurant"))
                .andExpect(jsonPath("$.location").value("Colombo"));
    }

//    TC-002
    @Test
    public void createRestaurant_InvalidToken_ShouldReturnUnauthorized() throws Exception {
        // Mock userService to throw an exception for an invalid token
        Mockito.when(userService.findUserByJwtToken("Bearer invalidToken")).thenThrow(new RuntimeException("Invalid token"));

        mockMvc.perform(post("/api/admin/restaurant")
                        .header("Authorization", "Bearer invalidToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(restaurantDTO)))
                .andExpect(status().isUnauthorized());
    }

//    TC-003
    @Test
    public void getAllRestaurants_ShouldReturnAllRestaurants() throws Exception {
        List<RestaurantDTO> restaurants = Arrays.asList(restaurantDTO);
        Mockito.when(restaurantService.getAllRestaurants()).thenReturn(restaurants);

        mockMvc.perform(get("/restaurant")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Restaurant"))
                .andExpect(jsonPath("$[0].location").value("Colombo"));
    }

//    TC-004
    @Test
    public void getRestaurantById_ValidId_ShouldReturnRestaurant() throws Exception {
        Mockito.when(restaurantService.getRestaurantById("rest123")).thenReturn(restaurantDTO);

        mockMvc.perform(get("/restaurant/rest123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Restaurant"));
    }

//    TC-005
    @Test
    public void getRestaurantById_InvalidId_ShouldReturnNotFound() throws Exception {
        Mockito.when(restaurantService.getRestaurantById("invalidId")).thenThrow(new RuntimeException("Restaurant not found"));

        mockMvc.perform(get("/restaurant/invalidId")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
