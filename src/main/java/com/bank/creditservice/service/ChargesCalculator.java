package com.bank.creditservice.service;

import com.bank.creditservice.model.document.Charge;
import com.bank.creditservice.model.document.CreditCard;
import com.bank.creditservice.model.document.Transaction;
import com.bank.creditservice.repository.TransactionRepository;
import reactor.core.publisher.Mono;

public interface ChargesCalculator {
    TransactionRepository getTransactionRepository();

    default Mono<Double> calculateTotalCharges(CreditCard creditCard) {
        return getTransactionRepository().findAll()
                .ofType(Charge.class)
                .filter(transaction -> transaction.getCreditCardNumber().equals(creditCard.getCardNumber()))
                .map(Transaction::getAmount)
                .reduce(0.0, Double::sum);
    }
}
