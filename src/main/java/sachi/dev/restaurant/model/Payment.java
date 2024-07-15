package sachi.dev.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "payments")
public class Payment {

    @Id
    private String paymentId;
    private String reservationId;
    private Double amount;
    private Date paymentDate;
    private String status;
    private String paymentType;

}
