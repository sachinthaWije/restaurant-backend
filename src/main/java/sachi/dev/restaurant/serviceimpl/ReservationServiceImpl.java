package sachi.dev.restaurant.serviceimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sachi.dev.restaurant.dto.PaymentDTO;
import sachi.dev.restaurant.dto.ReservationDTO;
import sachi.dev.restaurant.dto.RestaurantDTO;
import sachi.dev.restaurant.exception.CustomException;
import sachi.dev.restaurant.model.*;
import sachi.dev.restaurant.repository.*;
import sachi.dev.restaurant.service.ReservationService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ReservationDTO create(ReservationDTO reservationDTO) {
        if (isTableReserved(reservationDTO.getTableId(), reservationDTO.getReservationDate(), reservationDTO.getReservationTime())) {
            throw new CustomException("Table is already reserved for the selected date and time.", HttpStatus.CONFLICT);
        }
        Reservation reservation = modelMapper.map(reservationDTO, Reservation.class);
        reservation.setCreatedAt(LocalDateTime.now());
        reservationRepository.save(reservation);
        return modelMapper.map(reservation, ReservationDTO.class);
    }

    @Override
    public ReservationDTO findByReservationId(String reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException("Reservation not found", HttpStatus.NOT_FOUND));
        return modelMapper.map(reservation, ReservationDTO.class);
    }

    @Override
    public List<ReservationDTO> findReservationsByCustomerId(String customerId) {
        List<ReservationDTO> reservationDTOList = reservationRepository.findByCustomerId(customerId);
        for (ReservationDTO reservationDTO : reservationDTOList) {
            Optional<Restaurant> optionalRestaurantDTO = restaurantRepository.findById(reservationDTO.getRestaurantId());
            String restaurantName = optionalRestaurantDTO.get().getName() + " - " + optionalRestaurantDTO.get().getLocation();

            Optional<Table> optionalTable = tableRepository.findById(reservationDTO.getTableId());
            String tableNumber = optionalTable.get().getTableNumber();

            PaymentDTO paymentDTO = paymentRepository.findPaymentByReservationId(reservationDTO.getReservationId());

            reservationDTO.setRestaurantName(restaurantName);
            reservationDTO.setTableName(tableNumber);
            if (paymentDTO != null) {
                reservationDTO.setPaymentAmount(paymentDTO.getAmount());
            }


            System.out.println(restaurantName);

        }
        return reservationDTOList;
    }

    @Override
    public List<ReservationDTO> findReservationsByRestaurantId(String restaurantId) {
        List<ReservationDTO> reservationDTOList = reservationRepository.findByRestaurantId(restaurantId);
        for (ReservationDTO reservationDTO : reservationDTOList) {
            Optional<User> optUser = userRepository.findById(reservationDTO.getCustomerId());
            String username = optUser.get().getUsername();
            reservationDTO.setCustomerName(username);
            reservationDTO.setReservationType(reservationDTO.getReservationType().toLowerCase().replaceAll("_"," "));

           PaymentDTO paymentDTO= paymentRepository.findByReservationId(reservationDTO.getReservationId());
            if (paymentDTO != null) {
                reservationDTO.setPaymentAmount(paymentDTO.getAmount());
                reservationDTO.setPaymentType(paymentDTO.getPaymentType().toLowerCase().replaceAll("_"," "));
            }

            Optional<Table> optionalTable= tableRepository.findById(reservationDTO.getTableId());
            if (optionalTable.isPresent()) {
                reservationDTO.setTableName(optionalTable.get().getTableNumber());
            }
        }
        return reservationDTOList;
    }

    @Override
    public ReservationDTO update(String reservationId, ReservationDTO reservationDTO) {
        return null;
    }

    @Override
    public void updateReservationStatus(String reservationId, ReservationStatus reservationStatus) {
        reservationRepository.updateReservationStatus(reservationId, reservationStatus);
    }

    @Override
    public List<ReservationDTO> searchReservations(String restaurantId, LocalDate startDate,LocalDate endDate, String reservationType) {
        return reservationRepository.findByCriteria(restaurantId, startDate,endDate, reservationType);
    }

    private boolean isTableReserved(String tableId, LocalDate date, LocalTime time) {
        return reservationRepository.existsByTableIdAndReservationDateAndReservationTime(tableId, date, time);
    }
}
