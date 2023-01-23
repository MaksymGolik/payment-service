package com.app.paymentservice.service;

import com.app.paymentservice.dto.PaymentCreateRequest;
import com.app.paymentservice.dto.PaymentResponse;

public interface PaymentService {
    PaymentResponse save(PaymentCreateRequest paymentCreateRequest);
}
