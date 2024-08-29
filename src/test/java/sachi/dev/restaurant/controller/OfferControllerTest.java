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
import sachi.dev.restaurant.dto.OfferDTO;
import sachi.dev.restaurant.service.OfferService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OfferControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OfferService offerService;

    private OfferDTO offerDTO;

    @BeforeEach
    public void setUp() {
        offerDTO = new OfferDTO();
        offerDTO.setName("Summer Sale");
        offerDTO.setDiscountPercentage(20);
        offerDTO.setDescription("20% off on all items this summer!");
    }

//    TC-001
    @Test
    @WithMockUser(username = "staffUser", roles = {"STAFF"})
    public void createOffer_ValidStaffMember_ShouldReturnCreated() throws Exception {
        Mockito.when(offerService.createOffer(any(OfferDTO.class))).thenReturn(offerDTO);

        mockMvc.perform(post("/api/staff/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(offerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Summer Sale"));
    }

//    TC-002
    @Test
    @WithMockUser(username = "customerUser", roles = {"CUSTOMER"})
    public void createOffer_InvalidRole_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(post("/api/staff/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(offerDTO)))
                .andExpect(status().isForbidden());
    }

//    TC-003
    @Test
    public void createOffer_NoAuthentication_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(post("/api/staff/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(offerDTO)))
                .andExpect(status().isUnauthorized());
    }

//    TC-004
    @Test
    @WithMockUser(username = "staffUser", roles = {"STAFF"})
    public void updateOffer_ValidRequest_ShouldReturnUpdatedOffer() throws Exception {
        Mockito.when(offerService.updateOffer(eq("offerId"), any(OfferDTO.class))).thenReturn(offerDTO);

        mockMvc.perform(put("/api/staff/offer/offerId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(offerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Summer Sale"));
    }

//    TC-005
    @Test
    @WithMockUser(username = "staffUser", roles = {"STAFF"})
    public void getAllOffers_ShouldReturnListOfOffers() throws Exception {
        List<OfferDTO> offers = Arrays.asList(offerDTO);
        Mockito.when(offerService.findAllOffers()).thenReturn(offers);

        mockMvc.perform(get("/api/staff/offer")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Summer Sale"));
    }

//    TC-006
    @Test
    @WithMockUser(username = "staffUser", roles = {"STAFF"})
    public void getOfferById_ValidId_ShouldReturnOffer() throws Exception {
        Mockito.when(offerService.findOfferById("offerId")).thenReturn(offerDTO);

        mockMvc.perform(get("/api/staff/offer/offerId")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Summer Sale"));
    }
}
