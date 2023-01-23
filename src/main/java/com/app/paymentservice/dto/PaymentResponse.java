package com.app.paymentservice.dto;

import com.app.paymentservice.model.PaymentStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaymentResponse {
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private double paymentAmount;
    private PaymentStatus status;
    private Long ticketId;
}
