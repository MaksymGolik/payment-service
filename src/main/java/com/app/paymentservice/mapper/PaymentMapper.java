package com.app.paymentservice.mapper;

import com.app.paymentservice.dto.PaymentCreateRequest;
import com.app.paymentservice.dto.PaymentResponse;
import com.app.paymentservice.model.Payment;

public class PaymentMapper {
    public static Payment createDtoToModel(PaymentCreateRequest paymentCreateRequest){
        return Payment.builder()
                .firstName(paymentCreateRequest.getFirstName())
                .middleName(paymentCreateRequest.getMiddleName())
                .lastName(paymentCreateRequest.getLastName())
                .paymentAmount(paymentCreateRequest.getPaymentAmount())
                .ticketId(paymentCreateRequest.getTicketId())
                .build();
    }

    public static PaymentResponse modelToResponseDto(Payment payment){
        return PaymentResponse.builder()
                .id(payment.getId())
                .firstName(payment.getFirstName())
                .middleName(payment.getMiddleName())
                .lastName(payment.getLastName())
                .paymentAmount(payment.getPaymentAmount())
                .status(payment.getStatus().name())
                .ticketId(payment.getTicketId())
                .build();
    }
}
