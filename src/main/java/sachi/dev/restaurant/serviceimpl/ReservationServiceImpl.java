package sachi.dev.restaurant.serviceimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sachi.dev.restaurant.dto.ReservationDTO;
import sachi.dev.restaurant.exception.CustomException;
import sachi.dev.restaurant.model.Reservation;
import sachi.dev.restaurant.model.ReservationStatus;
import sachi.dev.restaurant.repository.ReservationRepository;
import sachi.dev.restaurant.service.ReservationService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ModelMapper modelMapper;

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
        return reservationRepository.findByCustomerId(customerId);
    }

    @Override
    public List<ReservationDTO> findReservationsByRestaurantId(String restaurantId) {
        return reservationRepository.findByRestaurantId(restaurantId);
    }

    @Override
    public ReservationDTO update(String reservationId, ReservationDTO reservationDTO) {
        return null;
    }

    @Override
    public void updateReservationStatus(String reservationId, ReservationStatus reservationStatus) {
        reservationRepository.updateReservationStatus(reservationId, reservationStatus);
    }

    private boolean isTableReserved(String tableId, LocalDate date, LocalTime time) {
        return reservationRepository.existsByTableIdAndReservationDateAndReservationTime(tableId, date, time);
    }
}
