package sachi.dev.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "reservations")
public class Reservation {

    @Id
    private String reservationId;
    private String customerId;
    private String restaurantId;
    private Date reservationDate;
    private String reservationType;
    private String reservationStatus;
    private int numberOfPeople;
    private String tableId;
    private List<String> orderMenuItems;
    private String paymentId;

    @CreatedDate
    private LocalDateTime createdAt;
}
