package com.bank.creditservice.service;

import com.bank.creditservice.mapper.TransactionMapper;
import com.bank.creditservice.model.document.Charge;
import com.bank.creditservice.model.document.CreditCardPayment;
import com.bank.creditservice.model.document.CreditPayment;
import com.bank.creditservice.model.dto.request.TransactionRequest;
import com.bank.creditservice.model.dto.response.OperationResponse;
import com.bank.creditservice.model.dto.response.TransactionResponse;
import com.bank.creditservice.repository.TransactionRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionService<T extends TransactionRequest> {
    Mono<OperationResponse> makeTransaction(T request);

    TransactionRepository getTransactionRepository();

    TransactionMapper getTransactionMapper();

    default Flux<TransactionResponse> findCreditPaymentsById(String id) {
        return getTransactionRepository().findAll()
                .ofType(CreditPayment.class)
                .filter(payment -> payment.getCreditId().equals(id))
                .map(getTransactionMapper()::toResponse);
    }

    default Flux<TransactionResponse> findCreditCardPaymentsByCardNumber(String cardNumber) {
        return getTransactionRepository().findAll()
                .ofType(CreditCardPayment.class)
                .filter(payment -> payment.getCreditCardNumber().equals(cardNumber))
                .map(getTransactionMapper()::toResponse);
    }

    default Flux<TransactionResponse> findCreditChargesByCardNumber(String cardNumber) {
        return getTransactionRepository().findAll()
                .ofType(Charge.class)
                .filter(payment -> payment.getCreditCardNumber().equals(cardNumber))
                .map(getTransactionMapper()::toResponse);
    }
}
