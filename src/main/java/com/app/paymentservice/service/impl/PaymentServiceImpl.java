package com.app.paymentservice.service.impl;

import com.app.paymentservice.dto.PaymentCreateRequest;
import com.app.paymentservice.dto.PaymentResponse;
import com.app.paymentservice.mapper.PaymentMapper;
import com.app.paymentservice.model.Payment;
import com.app.paymentservice.model.PaymentStatus;
import com.app.paymentservice.repository.PaymentRepository;
import com.app.paymentservice.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private PaymentRepository paymentRepository;
    private static final PaymentStatus NEW_STATUS = PaymentStatus.NEW;

    @Override
    public PaymentResponse save(PaymentCreateRequest paymentCreateRequest) {
        Payment payment = PaymentMapper.createDtoToModel(paymentCreateRequest);
        payment.setStatus(NEW_STATUS);
        return PaymentMapper.modelToResponseDto(paymentRepository.save(payment));
    }
}
