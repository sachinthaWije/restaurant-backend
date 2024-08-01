package sachi.dev.restaurant.dto;

import lombok.Data;

import java.util.Date;

@Data
public class PaymentDTO {

    private String paymentId;
    private String reservationId;
    private Double amount;
    private Date paymentDate;
    private String status;
    private String paymentType;
}
