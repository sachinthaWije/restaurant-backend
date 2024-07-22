package sachi.dev.restaurant.request;

import lombok.Data;
import sachi.dev.restaurant.model.ReservationStatus;

@Data
public class UpdateReservationStatusRequest {
    private ReservationStatus status;
}
