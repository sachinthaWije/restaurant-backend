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
import sachi.dev.restaurant.dto.PaymentDTO;
import sachi.dev.restaurant.service.PaymentService;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    private PaymentDTO paymentDTO;

    @BeforeEach
    public void setUp() {
        paymentDTO = new PaymentDTO();
        paymentDTO.setAmount(100.0);
        paymentDTO.setPaymentType("Credit Card");
        paymentDTO.setPaymentDate(new Date());
        paymentDTO.setStatus("PENDING");
        paymentDTO.setReservationId("66abd032b219b46832eccf36");
    }

//    TC-001
    @Test
    public void makePayment_ValidPayment_ShouldReturnCreated() throws Exception {
        Mockito.when(paymentService.makePayment(any(PaymentDTO.class))).thenReturn(paymentDTO);

        mockMvc.perform(post("/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(paymentDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(100.0))
                .andExpect(jsonPath("$.paymentType").value("Credit Card"));
    }

//    TC-002
    @Test
    public void makePayment_InvalidPayment_ShouldReturnBadRequest() throws Exception {
        // Invalid payment with a negative amount
        paymentDTO.setAmount(-50.0);

        mockMvc.perform(post("/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(paymentDTO)))
                .andExpect(status().isBadRequest());
    }
}
