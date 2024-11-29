package com.bank.creditservice.controller;

import com.bank.creditservice.model.dto.request.ChargeRequest;
import com.bank.creditservice.model.dto.request.CreditCardPaymentRequest;
import com.bank.creditservice.model.dto.request.CreditPaymentRequest;
import com.bank.creditservice.model.dto.response.OperationResponse;
import com.bank.creditservice.model.dto.response.TransactionResponse;
import com.bank.creditservice.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class TransactionController {
    private final TransactionService<CreditPaymentRequest> creditPaymentService;
    private final TransactionService<ChargeRequest> chargeService;
    private final TransactionService<CreditCardPaymentRequest> creditCardPaymentService;

    @PostMapping("credit-payments")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<OperationResponse> makeCreditPayment(@RequestBody @Valid CreditPaymentRequest request) {
        return creditPaymentService.makeTransaction(request);
    }

    @PostMapping("credit-card-charges")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<OperationResponse> makeCreditCardCharge(@RequestBody @Valid ChargeRequest request) {
        return chargeService.makeTransaction(request);
    }

    @PostMapping("credit-card-payments")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<OperationResponse> makeCreditCardPayment(@RequestBody @Valid CreditCardPaymentRequest request) {
        return creditCardPaymentService.makeTransaction(request);
    }

    @GetMapping("credit-payments/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Flux<TransactionResponse> findCreditPaymentsById(@PathVariable String id) {
        return creditPaymentService.findCreditPaymentsById(id);
    }

    @GetMapping("credit-card-payments/{cardNumber}")
    @ResponseStatus(HttpStatus.OK)
    public Flux<TransactionResponse> findCreditCardPaymentsByCardNumber(@PathVariable String cardNumber) {
        return creditCardPaymentService.findCreditCardPaymentsByCardNumber(cardNumber);
    }

    @GetMapping("credit-card-charges/{cardNumber}")
    @ResponseStatus(HttpStatus.OK)
    public Flux<TransactionResponse> findCreditChargesByCardNumber(@PathVariable String cardNumber) {
        return chargeService.findCreditChargesByCardNumber(cardNumber);
    }
}
