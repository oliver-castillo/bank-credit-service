package com.bank.creditservice.service.implementation;

import com.bank.creditservice.exception.NotFoundException;
import com.bank.creditservice.mapper.TransactionMapper;
import com.bank.creditservice.model.document.CreditCard;
import com.bank.creditservice.model.dto.request.ChargeRequest;
import com.bank.creditservice.model.dto.response.OperationResponse;
import com.bank.creditservice.repository.CreditCardRepository;
import com.bank.creditservice.repository.TransactionRepository;
import com.bank.creditservice.service.ChargesCalculator;
import com.bank.creditservice.service.CreditCardFinder;
import com.bank.creditservice.service.TransactionService;
import com.bank.creditservice.util.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@Service
public class ChargeService implements TransactionService<ChargeRequest>, CreditCardFinder, ChargesCalculator {
    private final CreditCardRepository creditCardRepository;
    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;

    @Override
    public Mono<OperationResponse> makeTransaction(ChargeRequest request) {
        return processCharge(request)
                .then(Mono.just(new OperationResponse(
                        ResponseMessage.CREATED_SUCCESSFULLY,
                        HttpStatus.CREATED)));
    }

    private Mono<Void> processCharge(ChargeRequest request) {
        Mono<CreditCard> foundCreditCard = getCreditCard(request.getClientId(), request.getCreditCardNumber());
        Mono<Double> totalCharges = foundCreditCard.flatMap(this::calculateTotalCharges);
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

    @Override
    public TransactionRepository getTransactionRepository() {
        return transactionRepository;
    }

    @Override
    public TransactionMapper getTransactionMapper() {
        return transactionMapper;
    }

    @Override
    public CreditCardRepository getCreditCardRepository() {
        return creditCardRepository;
    }
}
