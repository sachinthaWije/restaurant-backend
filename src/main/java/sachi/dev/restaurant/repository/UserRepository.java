package sachi.dev.restaurant.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import sachi.dev.restaurant.dto.UserDTO;
import sachi.dev.restaurant.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {


    User findByUsername(String username);

    Boolean existsByUsername(String username);

    @Query("{'role' :  'ROLE_STAFF'}")
    List<UserDTO> findByRoleStaff();
}
