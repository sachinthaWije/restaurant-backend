package sachi.dev.restaurant.dto;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class OfferDTO {

    private String offerId;
    private String name;
    private String description;
    private Integer discountPercentage;
    private Date startDate;
    private Date endDate;

    private Date createdAt;
}
