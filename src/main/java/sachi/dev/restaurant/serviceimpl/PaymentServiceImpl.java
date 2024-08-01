package sachi.dev.restaurant.serviceimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sachi.dev.restaurant.dto.PaymentDTO;
import sachi.dev.restaurant.model.Payment;
import sachi.dev.restaurant.repository.PaymentRepository;
import sachi.dev.restaurant.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PaymentDTO makePayment(PaymentDTO paymentDTO) {
        Payment payment = modelMapper.map(paymentDTO, Payment.class);
        return modelMapper.map(paymentRepository.save(payment), PaymentDTO.class);
    }
}
