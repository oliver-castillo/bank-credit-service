package com.bank.creditservice.service;

import com.bank.creditservice.exception.NotFoundException;
import com.bank.creditservice.mapper.TransactionMapper;
import com.bank.creditservice.model.document.Charge;
import com.bank.creditservice.model.document.CreditCard;
import com.bank.creditservice.model.document.Transaction;
import com.bank.creditservice.model.dto.request.ChargeRequest;
import com.bank.creditservice.model.dto.response.OperationResponse;
import com.bank.creditservice.repository.CreditCardRepository;
import com.bank.creditservice.repository.TransactionRepository;
import com.bank.creditservice.util.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@Service
public class ChargeService implements TransactionService<ChargeRequest> {
    private final CreditCardRepository creditCardRepository;
    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;

    @Override
    public Mono<OperationResponse> makeTransaction(ChargeRequest request) {
        return processCharge(request)
                //.flatMap(this::updateCreditCardStatus)
                .then(Mono.just(new OperationResponse(
                        ResponseMessage.CREATED_SUCCESSFULLY,
                        HttpStatus.CREATED)));
    }

    private Mono<CreditCard> findCreditCard(ChargeRequest request) {
        return creditCardRepository.findByClientId(request.getClientId())
                .switchIfEmpty(Mono.error(new NotFoundException("El cliente no cuenta con una tarjeta de crédito")))
                .filter(creditCard -> creditCard.getCardNumber().equals(request.getCreditCardNumber()))
                .switchIfEmpty(Mono.error(new NotFoundException("No se encontró la tarjeta de crédito")))
                .single();
    }

    private Mono<Double> getCharges(CreditCard creditCard) {
        return transactionRepository.findAll()
                .ofType(Charge.class)
                .filter(transaction -> transaction.getCreditCardNumber().equals(creditCard.getCardNumber()))
                .map(Transaction::getAmount)
                .reduce(0.0, Double::sum);
    }

    private Mono<Void> processCharge(ChargeRequest request) {
        Mono<CreditCard> foundCreditCard = findCreditCard(request);
        Mono<Double> totalCharges = foundCreditCard.flatMap(this::getCharges);
        return Mono.zip(foundCreditCard, totalCharges).flatMap(tuple -> {
            CreditCard creditCard = tuple.getT1();
            double totalChargesAmount = tuple.getT2();
            if (creditCard.canAddCharge(totalChargesAmount, request.getAmount())) {
                return transactionRepository.save(transactionMapper.toDocument(request))
                        .then();
            }
            return Mono.error(new NotFoundException("No se puede realizar el cargo"));
        });
    }

    /*private Mono<CreditCard> updateCreditCardStatus(CreditCard creditCard) {
        return getCharges(creditCard)
                .map(totalCharges -> {
                    creditCard.setStatus(creditCard.checkStatus(totalCharges));
                    return creditCard;
                })
                .flatMap(creditCardRepository::save);
    }*/
}
