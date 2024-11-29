package com.bank.creditservice.repository;

import com.bank.creditservice.model.document.CreditCard;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CreditCardRepository extends ReactiveMongoRepository<CreditCard, String> {
    Mono<CreditCard> findByClientIdAndCardNumber(String clientId, String cardNumber);

    Mono<CreditCard> findCreditCardByCardNumber(String cardNumber);
}
