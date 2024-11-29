package com.bank.creditservice.service.implementation;

import com.bank.creditservice.exception.BadRequestException;
import com.bank.creditservice.mapper.TransactionMapper;
import com.bank.creditservice.model.document.Credit;
import com.bank.creditservice.model.document.CreditPayment;
import com.bank.creditservice.model.dto.request.CreditPaymentRequest;
import com.bank.creditservice.model.dto.response.OperationResponse;
import com.bank.creditservice.model.enums.CreditStatus;
import com.bank.creditservice.repository.CreditRepository;
import com.bank.creditservice.repository.TransactionRepository;
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
public class CreditPaymentService implements TransactionService<CreditPaymentRequest> {
    private final CreditRepository creditRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public Mono<OperationResponse> makeTransaction(CreditPaymentRequest request) {
        return validateAndProcessPayment(request)
                .flatMap(credit -> updateCreditStatus(credit, request.getCreditId()))
                .then(Mono.just(new OperationResponse(
                        ResponseMessage.CREATED_SUCCESSFULLY,
                        HttpStatus.CREATED)));
    }

    @Override
    public TransactionRepository getTransactionRepository() {
        return transactionRepository;
    }

    @Override
    public TransactionMapper getTransactionMapper() {
        return transactionMapper;
    }

    private Mono<Credit> validateAndProcessPayment(CreditPaymentRequest request) {
        return findAndValidateCredit(request)
                .zipWith(getNumberOfPaymentsMade(request.getCreditId()))
                .flatMap(tuple -> processPayment(tuple.getT1(), tuple.getT2(), request));
    }

    private Mono<Credit> findAndValidateCredit(CreditPaymentRequest request) {
        return creditRepository.findByClientId(request.getClientId())
                .switchIfEmpty(Mono.error(new BadRequestException("No se encontraron créditos asignados al cliente")))
                .filter(credit -> credit.getCreditStatus() == CreditStatus.ACTIVE && credit.getId().equals(request.getCreditId()))
                .switchIfEmpty(Mono.error(new BadRequestException("El cliente no cuenta con créditos activos")))
                .single();
    }

    private Mono<Integer> getNumberOfPaymentsMade(String creditId) {
        return transactionRepository.findAll()
                .ofType(CreditPayment.class)
                .filter(payment -> payment.getCreditId().equals(creditId))
                .count()
                .map(Long::intValue);
    }

    private Mono<Credit> processPayment(Credit credit, int paymentsMade, CreditPaymentRequest request) {
        if (paymentsMade >= credit.getNumberOfPayments()) {
            return Mono.error(new BadRequestException("Se realizaron todos los pagos"));
        }

        double monthlyPayment = credit.calculateMonthlyPaymentAmount();
        if (request.getAmount() != monthlyPayment) {
            return Mono.error(new BadRequestException("El monto de pago mensual debe ser: S/. " + monthlyPayment));
        }

        CreditPayment payment = transactionMapper.toDocument(request);
        payment.setInterest(credit.calculateInterest());

        return transactionRepository.save(payment)
                .thenReturn(credit);
    }

    private Mono<Credit> updateCreditStatus(Credit credit, String creditId) {
        return getNumberOfPaymentsMade(creditId)
                .map(paymentsMade -> {
                    credit.setCreditStatus(credit.checkStatus(paymentsMade));
                    return credit;
                })
                .flatMap(creditRepository::save)
                .doOnSuccess(c -> log.info("Se actualizó el estado del crédito después del pago"));
    }
}
