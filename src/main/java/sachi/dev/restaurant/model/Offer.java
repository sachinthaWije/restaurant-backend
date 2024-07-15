package sachi.dev.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "offers")
public class Offer {

    @Id
    private String offerId;
    private String name;
    private String description;
    private Integer discountPercentage;
    private Date startDate;
    private Date endDate;

    @CreatedDate
    private LocalDateTime createdAt;
}
