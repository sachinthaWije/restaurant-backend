package sachi.dev.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class User {
    @Id
    private String userId;
    private String username;
    private String password;
    private String role;
    private String branchId;
    private ContactInfo contactInfo;

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
