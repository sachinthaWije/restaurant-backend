package sachi.dev.restaurant.dto;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import sachi.dev.restaurant.model.QueryStatus;

import java.util.Date;

@Data
public class QueryDTO {
    private String queryId;
    private String customerId;
    private String restaurantId;
    private String subject;
    private String message;
    private QueryStatus status;

    private String respondedMessage;
    private String respondedBy;
    private Date respondedAt;
}
