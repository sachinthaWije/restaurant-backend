package sachi.dev.restaurant.dto;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import sachi.dev.restaurant.model.QueryStatus;

import java.util.Date;

@Data
public class QueryDTO {
    private String queryId;
    private String customerId;
    private String customerName;
    private String restaurantId;
    private String restaurantName;
    private String subject;
    private String message;
    private QueryStatus status;

    private Date createdAt;
    private String respondedMessage;
    private String respondedBy;
    private Date respondedAt;
}
