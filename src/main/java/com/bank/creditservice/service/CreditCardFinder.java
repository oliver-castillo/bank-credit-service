package com.bank.creditservice.service;

import com.bank.creditservice.exception.NotFoundException;
import com.bank.creditservice.model.document.CreditCard;
import com.bank.creditservice.repository.CreditCardRepository;
import reactor.core.publisher.Mono;

public interface CreditCardFinder {
    CreditCardRepository getCreditCardRepository();

    default Mono<CreditCard> getCreditCard(String clientId, String cardNumber) {
        return getCreditCardRepository().findByClientIdAndCardNumber(clientId, cardNumber)
                .switchIfEmpty(Mono.error(new NotFoundException("No se encontró la tarjeta de crédito")));
    }
}
