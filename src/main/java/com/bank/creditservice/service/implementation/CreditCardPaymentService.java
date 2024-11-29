package com.bank.creditservice.service.implementation;

import com.bank.creditservice.exception.NotFoundException;
import com.bank.creditservice.mapper.TransactionMapper;
import com.bank.creditservice.model.document.CreditCard;
import com.bank.creditservice.model.document.CreditCardPayment;
import com.bank.creditservice.model.dto.request.CreditCardPaymentRequest;
import com.bank.creditservice.model.dto.response.OperationResponse;
import com.bank.creditservice.model.enums.CreditCardStatus;
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

@Slf4j
@RequiredArgsConstructor
@Service
public class CreditCardPaymentService implements TransactionService<CreditCardPaymentRequest>, CreditCardFinder, ChargesCalculator {
    private final CreditCardRepository creditCardRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public Mono<OperationResponse> makeTransaction(CreditCardPaymentRequest request) {
        return processPayment(request)
                .flatMap(this::updateCreditCardStatus)
                .then(Mono.just(new OperationResponse(
                        ResponseMessage.CREATED_SUCCESSFULLY,
                        HttpStatus.CREATED)));
    }

    private Mono<Double> calculateTotalPayments(CreditCard creditCard) {
        return transactionRepository.findAll()
                .ofType(CreditCardPayment.class)
                .filter(transaction -> transaction.getCreditCardNumber().equals(creditCard.getCardNumber()))
                .map(CreditCardPayment::getAmount)
                .reduce(0.0, Double::sum);
    }

    private Mono<CreditCard> processPayment(CreditCardPaymentRequest request) {
        Mono<CreditCard> foundCreditCard = getCreditCard(request.getClientId(), request.getCreditCardNumber())
                .filter(creditCard -> creditCard.getCreditCardStatus() == CreditCardStatus.UNPAID)
                .switchIfEmpty(Mono.error(new NotFoundException("No se encontró la tarjeta de crédito")));

        Mono<Double> totalCharges = foundCreditCard.flatMap(this::calculateTotalCharges);
        Mono<Double> totalPayments = foundCreditCard.flatMap(this::calculateTotalPayments);

        return Mono.zip(foundCreditCard, totalCharges, totalPayments).flatMap(tuple -> {
            CreditCard creditCard = tuple.getT1();
            double totalChargesAmount = tuple.getT2();
            double totalPaymentsAmount = tuple.getT3();
            double debt = totalChargesAmount - totalPaymentsAmount;

            if (request.getAmount() <= debt && request.getAmount() > 0) {
                return transactionRepository.save(transactionMapper.toDocument(request)).then(Mono.just(creditCard));
            }
            return Mono.error(new NotFoundException("No se puede realizar el cargo. La deuda es actual es de s/. " + debt));
        });
    }

    private Mono<Void> updateCreditCardStatus(CreditCard creditCard) {
        return calculateTotalCharges(creditCard)
                .map(totalCharges -> {
                    creditCard.setCreditCardStatus(creditCard.checkStatus(totalCharges));
                    return creditCard;
                })
                .flatMap(creditCardRepository::save).then();
    }


    @Override
    public CreditCardRepository getCreditCardRepository() {
        return creditCardRepository;
    }

    @Override
    public TransactionRepository getTransactionRepository() {
        return transactionRepository;
    }

    @Override
    public TransactionMapper getTransactionMapper() {
        return transactionMapper;
    }
}
