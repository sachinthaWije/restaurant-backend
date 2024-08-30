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
import sachi.dev.restaurant.dto.ReservationDTO;
import sachi.dev.restaurant.dto.UserDTO;
import sachi.dev.restaurant.model.ReservationStatus;
import sachi.dev.restaurant.service.ReservationService;
import sachi.dev.restaurant.service.UserService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ReservationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private UserService userService;

    private ReservationDTO reservationDTO;
    private UserDTO userDTO;
    private String validToken = "eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE3MjUwNDA0NTgsImV4cCI6MTcyNTEyNjg1OCwiZW1haWwiOiJwZXJlcmEiLCJhdXRob3JpdGllcyI6IlJPTEVfQ1VTVE9NRVIifQ.fa74WvkFCQL2SvnPc3vHHVwm7CFCFec5VY8DBQgEvI0xW-begOpvdd-ke5-jgYzx2j4lxkE20IM1A0p8g36CUA";

    @BeforeEach
    public void setUp() {
        reservationDTO = new ReservationDTO();
        reservationDTO.setRestaurantId("66995b81cdb5176c669efbb4");
        reservationDTO.setCustomerId("6698053dd3ea69112c3afc33");
        reservationDTO.setTableId("669a94951e86d154bf6a728f");
        reservationDTO.setReservationStatus(ReservationStatus.PENDING);

        userDTO = new UserDTO();
        userDTO.setUserId("customer123");
    }


//    TC-001
    @Test
    public void createReservation_ValidRequest_ShouldReturnCreated() throws Exception {

        Mockito.when(userService.findUserByJwtToken("Bearer " + validToken)).thenReturn(userDTO);
        Mockito.when(reservationService.create(any(ReservationDTO.class))).thenReturn(reservationDTO);

        mockMvc.perform(post("/api/customer/reservation")
                        .header("Authorization", "Bearer "+validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(reservationDTO)))
                .andExpect(status().isCreated())

                .andExpect(jsonPath("$.customerId").value("6698053dd3ea69112c3afc33"));
    }

//    TC-002
    @Test
    public void createReservation_InvalidToken_ShouldReturnUnauthorized() throws Exception {

        Mockito.when(userService.findUserByJwtToken("Bearer invalidToken")).thenThrow(new RuntimeException("Invalid token"));

        mockMvc.perform(post("/api/customer/reservation")
                        .header("Authorization", "Bearer invalidToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(reservationDTO)))
                .andExpect(status().isUnauthorized());
    }


//    TC-003
    @Test
    public void getAllReservations_InvalidToken_ShouldReturnUnauthorized() throws Exception {

        Mockito.when(userService.findUserByJwtToken("Bearer invalidToken")).thenThrow(new RuntimeException("Invalid token"));

        mockMvc.perform(get("/api/customer/reservation")
                        .header("Authorization", "Bearer invalidToken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
