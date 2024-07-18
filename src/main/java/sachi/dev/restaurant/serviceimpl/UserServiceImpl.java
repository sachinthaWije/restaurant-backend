package sachi.dev.restaurant.serviceimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sachi.dev.restaurant.config.JwtProvider;
import sachi.dev.restaurant.dto.UserDTO;
import sachi.dev.restaurant.exception.ResourceNotFoundException;
import sachi.dev.restaurant.model.Restaurant;
import sachi.dev.restaurant.model.User;
import sachi.dev.restaurant.repository.RestaurantRepository;
import sachi.dev.restaurant.repository.UserRepository;
import sachi.dev.restaurant.service.UserService;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        if(userDTO.getRestaurantId()!=null){
            Restaurant restaurant = restaurantRepository.findById(userDTO.getRestaurantId())
                    .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

        }
        User user = modelMapper.map(userDTO, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(new Date());
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public UserDTO getUserById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return modelMapper.map(user, UserDTO.class);
    }




    @Override
    public UserDTO updateUser(String userId, UserDTO userDTO) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        modelMapper.map(userDTO, existingUser);
        existingUser.setUserId(userId); // Ensure the ID doesn't change

        User updatedUser = userRepository.save(existingUser);
        return modelMapper.map(updatedUser, UserDTO.class);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return List.of();
    }

    @Override
    public List<UserDTO> getAllUsersByRestaurantId() {
        return List.of();
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public UserDTO findUserByJwtToken(String jwt) throws Exception {
        String username=jwtProvider.getEmailFromToken(jwt);
        User user=userRepository.findByUsername(username);
        if(user==null){
            throw new ResourceNotFoundException("User not found with id: " + username);
        }
        return modelMapper.map(user, UserDTO.class);
    }

}
