package sachi.dev.restaurant.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "queries")
public class Query {

    @Id
    private String queryId;
    private String customerId;
    private String restaurantId;
    private String subject;
    private String message;
    private String status;

    @CreatedDate
    private String createdAt;

    private String respondedBy;
    private Date respondedAt;

}
