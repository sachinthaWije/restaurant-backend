package sachi.dev.restaurant.repository.custom;

import com.mongodb.client.result.UpdateResult;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import sachi.dev.restaurant.dto.PaymentDTO;
import sachi.dev.restaurant.dto.ReservationDTO;
import sachi.dev.restaurant.model.Reservation;
import sachi.dev.restaurant.model.ReservationStatus;
import sachi.dev.restaurant.model.Table;
import sachi.dev.restaurant.model.User;
import sachi.dev.restaurant.repository.PaymentRepository;
import sachi.dev.restaurant.repository.RestaurantRepository;
import sachi.dev.restaurant.repository.TableRepository;
import sachi.dev.restaurant.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class CustomReservationRepositoryImpl implements CustomReservationRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    public ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public void updateReservationStatus(String reservationId, ReservationStatus status) {
        Query query = new Query(Criteria.where("_id").is(reservationId));
        Update update = new Update().set("reservationStatus", status);
        UpdateResult result = mongoTemplate.updateFirst(query, update, Reservation.class);
        if (result.getMatchedCount() == 0) {
            throw new RuntimeException("Reservation not found with id: " + reservationId);
        }
    }

    @Override
    public List<ReservationDTO> findByCriteria(String restaurantId, LocalDate startDate, LocalDate endDate, String reservationType) {
        Query query = new Query();

        if (restaurantId != null && !restaurantId.isEmpty()) {
            query.addCriteria(Criteria.where("restaurantId").is(restaurantId));
        }
        if (startDate != null && endDate != null) {
            query.addCriteria(Criteria.where("reservationDate").gte(startDate).lte(endDate));
        }
        if (reservationType != null && !reservationType.isEmpty()) {
            query.addCriteria(Criteria.where("reservationType").is(reservationType));
        }
        List<Reservation> reservations = mongoTemplate.find(query, Reservation.class);
        return reservations.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ReservationDTO convertToDto(Reservation reservation) {
        ReservationDTO dto = new ReservationDTO();
        dto.setReservationId(reservation.getReservationId());
        dto.setCustomerId(reservation.getCustomerId());
        dto.setRestaurantId(reservation.getRestaurantId());
        dto.setReservationDate(reservation.getReservationDate());
        dto.setReservationTime(reservation.getReservationTime());
        dto.setReservationType(reservation.getReservationType());
        dto.setNumberOfPeople(reservation.getNumberOfPeople());

        Optional<User> optUser = userRepository.findById(reservation.getCustomerId());
        String username = optUser.get().getUsername();
        dto.setCustomerName(username);
        dto.setReservationType(reservation.getReservationType().toLowerCase().replaceAll("_"," "));

        PaymentDTO paymentDTO= paymentRepository.findByReservationId(reservation.getReservationId());
        if (paymentDTO != null) {
            dto.setPaymentAmount(paymentDTO.getAmount());
            dto.setPaymentType(paymentDTO.getPaymentType().toLowerCase().replaceAll("_"," "));
        }

        dto.setRestaurantName(restaurantRepository.findById(reservation.getRestaurantId()).get().getLocation());

        Optional<Table> optionalTable= tableRepository.findById(reservation.getTableId());
        if (optionalTable.isPresent()) {
            dto.setTableName(optionalTable.get().getTableNumber());
        }
        return dto;
    }
}
