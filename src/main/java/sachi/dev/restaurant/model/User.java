package sachi.dev.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class User{
    @Id
    private String userId;
    private String username;
    private String password;
    private USER_ROLE role=USER_ROLE.ROLE_CUSTOMER;
    private String restaurantId;
    private ContactInfo contactInfo;

    @CreatedDate
    private Date createdAt;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ContactInfo{
        private String phone;
        private String email;
    }


}
