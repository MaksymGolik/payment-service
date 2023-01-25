package com.app.paymentservice.repository;

import com.app.paymentservice.model.Payment;
import com.app.paymentservice.model.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findPaymentByTicketId(Long ticketId);
    Page<Payment> findAllByStatus(Pageable page, PaymentStatus status);
}
