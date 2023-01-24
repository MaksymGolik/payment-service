package com.app.paymentservice.controller;

import com.app.paymentservice.dto.PaymentCreateRequest;
import com.app.paymentservice.dto.PaymentResponse;
import com.app.paymentservice.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/payments")
@AllArgsConstructor
public class PaymentController {

    private PaymentService paymentService;

    @PostMapping("/makePayment")
    public ResponseEntity<PaymentResponse> makePayment(@Valid @RequestBody PaymentCreateRequest paymentCreateRequest){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(paymentService.save(paymentCreateRequest));
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable("ticketId") Long ticketId){
        return ResponseEntity
                .ok(paymentService.findPaymentByTicketId(ticketId));
    }
}
