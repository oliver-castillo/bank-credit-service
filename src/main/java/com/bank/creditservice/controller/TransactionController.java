package com.bank.creditservice.controller;

import com.bank.creditservice.model.dto.request.CreditPaymentRequest;
import com.bank.creditservice.model.dto.response.OperationResponse;
import com.bank.creditservice.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class TransactionController {
    private final TransactionService<CreditPaymentRequest> creditPaymentService;

    @PostMapping("/credit-payment")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<OperationResponse> makeCreditPayment(@RequestBody @Valid CreditPaymentRequest request) {
        return creditPaymentService.makeTransaction(request);
    }
}
