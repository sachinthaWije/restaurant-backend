package sachi.dev.restaurant.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import sachi.dev.restaurant.model.Payment;

public interface PaymentRepository extends MongoRepository<Payment, String> {
}
