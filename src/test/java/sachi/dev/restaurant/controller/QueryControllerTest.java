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
import sachi.dev.restaurant.dto.QueryDTO;
import sachi.dev.restaurant.dto.UserDTO;
import sachi.dev.restaurant.model.QueryStatus;
import sachi.dev.restaurant.service.QueryService;
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
public class QueryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QueryService queryService;

    @MockBean
    private UserService userService;

    @MockBean
    private RestaurantService restaurantService;

    private QueryDTO queryDTO;
    private UserDTO userDTO;

    @BeforeEach
    public void setUp() {
        queryDTO = new QueryDTO();
        queryDTO.setQueryId("123454");
        queryDTO.setMessage("What are the opening hours?");
        queryDTO.setRespondedMessage("We are open from 9 AM to 9 PM");
        queryDTO.setSubject("Subject");
        queryDTO.setStatus(QueryStatus.PENDING);
        queryDTO.setCustomerId("66b62049a4f80e04a97df633");

        userDTO = new UserDTO();
        userDTO.setUserId("66b62049a4f80e04a97df633");
    }

    String token = "eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE3MjUwMzY5NTIsImV4cCI6MTcyNTEyMzM1MiwiZW1haWwiOiJwZXJlcmEiLCJhdXRob3JpdGllcyI6IlJPTEVfQ1VTVE9NRVIifQ.S5lOwNwS79kvEr4Q5yp-wHAVAoJA86df7KOBQd9eyIhqsNY5QKTBDj-6zaVeU0uPwaNdv8PFh-iFEYwaM3cNvw";

    //    TC-001
    @Test
    public void save_ValidCustomerRequest_ShouldReturnCreated() throws Exception {
        Mockito.when(userService.findUserByJwtToken("Bearer " + token)).thenReturn(userDTO);
        Mockito.when(queryService.save(any(QueryDTO.class))).thenReturn(queryDTO);

        mockMvc.perform(post("/api/customer/query/66aa726fb2359b136a62ab48")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(queryDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("What are the opening hours?"));
    }

    //    TC-002
    @Test
    public void save_InvalidToken_ShouldReturnUnauthorized() throws Exception {
        Mockito.when(userService.findUserByJwtToken("Bearer " + token)).thenThrow(new RuntimeException("Invalid token"));

        mockMvc.perform(post("/api/customer/query/66aa726fb2359b136a62ab48")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(queryDTO)))
                .andExpect(status().isUnauthorized());
    }

    //    TC-003
    @Test
    public void update_ValidStaffRequest_ShouldReturnUpdatedQuery() throws Exception {
        Mockito.when(userService.findUserByJwtToken("Bearer " + token)).thenReturn(userDTO);
        Mockito.when(queryService.updateRespond(any(QueryDTO.class), eq("123454"))).thenReturn(queryDTO);

        mockMvc.perform(post("/api/staff/update-query/123454")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(queryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.respondedMessage").value("We are open from 9 AM to 9 PM"));
    }

    //    TC-004
    @Test
    public void findAllByCustomerId_ValidCustomerRequest_ShouldReturnQueries() throws Exception {
        List<QueryDTO> queries = Arrays.asList(queryDTO);
        Mockito.when(userService.findUserByJwtToken("Bearer " + token)).thenReturn(userDTO);
        Mockito.when(queryService.findAllByCustomerId("66b62049a4f80e04a97df633")).thenReturn(queries);

        mockMvc.perform(get("/api/customer/queries")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].message").value("What are the opening hours?"));
    }

}
