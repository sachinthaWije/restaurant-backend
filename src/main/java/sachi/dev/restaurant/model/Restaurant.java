package sachi.dev.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "restaurants")
public class Restaurant {

    @Id
    private String restaurantId;
    private String name;
    private String location;
    private String adminId;
    private ContactInfo contactInfo;
    private List<String> photoGallery;
    private List<String> facilities;

    @CreatedDate
    private LocalDateTime createdAt;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ContactInfo{
        private String phone;
        private String email;
    }
}
