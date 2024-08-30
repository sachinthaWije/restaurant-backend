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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import sachi.dev.restaurant.dto.ReservationDTO;
import sachi.dev.restaurant.model.ReservationStatus;
import sachi.dev.restaurant.request.UpdateReservationStatusRequest;
import sachi.dev.restaurant.service.ReservationService;

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
public class ReservationAdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    private ReservationDTO reservationDTO;

    @BeforeEach
    public void setUp() {
        reservationDTO = new ReservationDTO();
        reservationDTO.setRestaurantId("66995b81cdb5176c669efbb4");
        reservationDTO.setCustomerId("6698053dd3ea69112c3afc33");
        reservationDTO.setTableId("669a94951e86d154bf6a728f");
        reservationDTO.setReservationDate(LocalDate.of(2024, 9, 1));
        reservationDTO.setReservationStatus(ReservationStatus.PENDING);
    }

    String token="eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE3MjUwMzg4NzIsImV4cCI6MTcyNTEyNTI3MiwiZW1haWwiOiJqb25fc3RhZmYiLCJhdXRob3JpdGllcyI6IlJPTEVfU1RBRkYifQ.eWVdmsgmAxQLTvgCfPTxy5jlrq2nJoI160sCPKzKpmTDTvYI09R9giR63u9ltefxXsFWmXVBfqtAYx5_2ssL7g";


//    TC-001
    @Test
    public void getAllReservations_ValidRequest_ShouldReturnReservations() throws Exception {
        List<ReservationDTO> reservations = Arrays.asList(reservationDTO);
        Mockito.when(reservationService.findReservationsByRestaurantId("66995b81cdb5176c669efbb4")).thenReturn(reservations);

        mockMvc.perform(get("/api/staff/reservation/66995b81cdb5176c669efbb4")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].restaurantId").value("66995b81cdb5176c669efbb4"))
                .andExpect(jsonPath("$[0].customerId").value("6698053dd3ea69112c3afc33"));
    }

//    TC-002
    @Test
    public void updateReservationStatus_ValidRequest_ShouldReturnOk() throws Exception {
        UpdateReservationStatusRequest request = new UpdateReservationStatusRequest();
        request.setStatus(ReservationStatus.PENDING);

        Mockito.doNothing().when(reservationService).updateReservationStatus("res123", ReservationStatus.COMPLETED);

        mockMvc.perform(post("/api/staff/reservation/update-status/res123")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());
    }


//    TC-003
    @Test
    public void searchReservations_ValidRequest_ShouldReturnReservations() throws Exception {
        List<ReservationDTO> reservations = Arrays.asList(reservationDTO);
        LocalDate startDate = LocalDate.of(2024, 9, 1);
        LocalDate endDate = LocalDate.of(2024, 9, 30);

        Mockito.when(reservationService.searchReservations("rest123", startDate, endDate, "DINE_IN"))
                .thenReturn(reservations);

        mockMvc.perform(get("/api/staff/reservation/search-reservations")
                        .param("restaurantId", "rest123")
                        .param("startDate", "2024-09-01")
                        .param("endDate", "2024-09-30")
                        .param("reservationType", "DINE_IN")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value("6698053dd3ea69112c3afc33"));
    }
}
