package sachi.dev.restaurant.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import sachi.dev.restaurant.model.ReservationStatus;
import sachi.dev.restaurant.model.ReservationType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Data
public class ReservationDTO {

    private String reservationId;

    @NotNull(message = "Restaurant ID is required")
    private String customerId;

    @NotNull(message = "Restaurant ID is required")
    private String restaurantId;

    @NotNull(message = "Reservation date is required")
    @FutureOrPresent(message = "Reservation date must be in the present or future")
    private LocalDate reservationDate;

    @NotNull(message = "Reservation date is required")
    private LocalTime reservationTime;



    @NotNull(message = "Reservation type is required")
    private ReservationType reservationType;

    @NotNull(message = "Reservation status is required")
    private ReservationStatus reservationStatus;

    @Min(value = 1, message = "Number of people must be at least 1")
    private int numberOfPeople;

    @NotNull(message = "Table ID is required")
    private String tableId;

    private List<String> orderMenuItems;
    private String paymentId;


    private LocalDateTime createdAt;

}
