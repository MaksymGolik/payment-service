package com.app.paymentservice.service.impl;

import com.app.paymentservice.dto.PaymentCreateRequest;
import com.app.paymentservice.dto.PaymentResponse;
import com.app.paymentservice.mapper.PaymentMapper;
import com.app.paymentservice.model.Payment;
import com.app.paymentservice.model.PaymentStatus;
import com.app.paymentservice.repository.PaymentRepository;
import com.app.paymentservice.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;

@Service
public class PaymentServiceImpl implements PaymentService {

    private PaymentRepository paymentRepository;
    private RestTemplate restTemplate;
    private static final PaymentStatus NEW_STATUS = PaymentStatus.NEW;
    private static final List<PaymentStatus> STATUSES_AFTER_NEW =
            List.of(PaymentStatus.FAILED, PaymentStatus.DONE);
    private static final Random random = new Random();

    @Autowired
    public PaymentServiceImpl(@Value("${ticketservice.base.url}") String baseUrl,
                              RestTemplateBuilder restTemplateBuilder,
                              PaymentRepository paymentRepository){
        restTemplate = restTemplateBuilder.rootUri(baseUrl).build();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        this.paymentRepository = paymentRepository;
    }

    @Override
    public PaymentResponse save(PaymentCreateRequest paymentCreateRequest) {
        Payment payment = PaymentMapper.createDtoToModel(paymentCreateRequest);
        payment.setStatus(NEW_STATUS);
        return PaymentMapper.modelToResponseDto(paymentRepository.save(payment));
    }

    @Override
    public PaymentResponse findPaymentByTicketId(Long ticketId) {
        return PaymentMapper.modelToResponseDto(paymentRepository
                .findPaymentByTicketId(ticketId)
                .orElseThrow(()-> new IllegalArgumentException("No payment found by ticket id " + ticketId)));
    }

    @Scheduled(fixedDelayString = "${payment.checking.in.milliseconds}")
    @Override
    public void checkNewPayments() {
        List<Payment> newPayments;
        int page = 0;
        int pageSize = 25;
        do{
            newPayments = paymentRepository
                    .findAllByStatus(PageRequest.of(page++,pageSize), NEW_STATUS)
                    .stream().toList();
            paymentRepository.saveAll(generatePaymentStatusForNewPayments(newPayments));
            if (!newPayments.isEmpty()) {
                restTemplate.patchForObject("/routes/cancel_ticket_occupation",
                        newPayments.stream()
                                .filter(payment ->
                                        payment.getStatus().equals(PaymentStatus.FAILED))
                                .map(Payment::getTicketId).toList(), Boolean.class);
            }
        }while(newPayments.size()==pageSize);
    }

    private List<Payment> generatePaymentStatusForNewPayments(List<Payment> newPayments){
        newPayments.forEach(payment -> payment.setStatus(STATUSES_AFTER_NEW.get(random.nextInt(2))));
        return newPayments;
    }
}
