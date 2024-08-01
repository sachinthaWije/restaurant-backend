package sachi.dev.restaurant.service;

import sachi.dev.restaurant.dto.PaymentDTO;

public interface PaymentService {

    PaymentDTO makePayment(PaymentDTO paymentDTO);
}
