package sachi.dev.restaurant.repository.custom;

import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import sachi.dev.restaurant.model.Reservation;
import sachi.dev.restaurant.model.ReservationStatus;

@Repository
public class CustomReservationRepositoryImpl implements CustomReservationRepository{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void updateReservationStatus(String reservationId, ReservationStatus status) {
        Query query = new Query(Criteria.where("_id").is(reservationId));
        Update update = new Update().set("reservationStatus", status);
        UpdateResult result = mongoTemplate.updateFirst(query, update, Reservation.class);
        if (result.getMatchedCount() == 0) {
            throw new RuntimeException("Reservation not found with id: " + reservationId);
        }
    }
}
