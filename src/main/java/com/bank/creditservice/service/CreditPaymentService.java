package com.bank.creditservice.service;

import com.bank.creditservice.exception.BadRequestException;
import com.bank.creditservice.mapper.TransactionMapper;
import com.bank.creditservice.model.document.Credit;
import com.bank.creditservice.model.document.CreditPayment;
import com.bank.creditservice.model.dto.request.CreditPaymentRequest;
import com.bank.creditservice.model.dto.response.OperationResponse;
import com.bank.creditservice.model.enums.CreditStatus;
import com.bank.creditservice.repository.CreditRepository;
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
public class CreditPaymentService implements TransactionService<CreditPaymentRequest> {
    private final CreditRepository creditRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public Mono<OperationResponse> makeTransaction(CreditPaymentRequest request) {
        return makeCreditPayment(request)
                .doOnSuccess(transaction -> log.info("Se registró el pago"))
                .then(Mono.fromCallable(() -> new OperationResponse(
                        ResponseMessage.CREATED_SUCCESSFULLY,
                        HttpStatus.CREATED)));
    }

    public Mono<Void> makeCreditPayment(CreditPaymentRequest request) {
        /*return creditRepository.findByClientId(request.getClientId())
                .switchIfEmpty(Mono.error(new BadRequestException("No se encontraron créditos asignados al cliente")))
                .filter(data -> data.getStatus() == CreditStatus.ACTIVE)
                .switchIfEmpty(Mono.error(new BadRequestException("El cliente no cuenta con créditos activos")))
                .filter(credit -> credit.getId().equals(request.getCreditId()))
                .switchIfEmpty(Mono.error(new BadRequestException("No se encontró el crédito"))).single()
                .flatMap(credit -> {
                    if (credit.getStatus() == CreditStatus.PAID) {
                        return Mono.error(new BadRequestException("El crédito ya fue pagado"));
                    }

                    double monthlyPaymentAmount = credit.calculateMonthlyPaymentAmount();
                    double paymentAmount = request.getAmount();

                    if (paymentAmount != monthlyPaymentAmount) {
                        return Mono.error(new BadRequestException("El monto de pago mensual debe ser: S/. " + monthlyPaymentAmount));
                    }

                    double interest = credit.calculateInterest();

                    credit.setStatus(credit.checkStatus());

                    return creditRepository.save(credit).then(Mono.just(interest));
                });*/
        Mono<Credit> foundCredit = creditRepository.findByClientId(request.getClientId())
                .switchIfEmpty(Mono.error(new BadRequestException("No se encontraron créditos asignados al cliente")))
                .filter(data -> data.getStatus() == CreditStatus.ACTIVE)
                .switchIfEmpty(Mono.error(new BadRequestException("El cliente no cuenta con créditos activos")))
                .filter(c -> c.getId().equals(request.getCreditId()))
                .switchIfEmpty(Mono.error(new BadRequestException("No se encontró el crédito"))).single();

        Mono<Long> numberOfPaymentsMade = transactionRepository.findAll()
                .map(CreditPayment.class::cast)
                .filter(transaction -> transaction.getCreditId().equals(request.getCreditId()))
                .count();

        return Mono.zip(foundCredit, numberOfPaymentsMade)
                .flatMap(tuple -> {
                    Credit credit = tuple.getT1();
                    int paymentsMade = (int) (long) tuple.getT2();

                    double monthlyPaymentAmount = credit.calculateMonthlyPaymentAmount();
                    double paymentAmount = request.getAmount();
                    double interest = credit.calculateInterest();

                    if (paymentsMade == credit.getNumberOfPayments()) {
                        return Mono.error(new BadRequestException("Se realizaron todos los pagos"));
                    }

                    if (paymentAmount != monthlyPaymentAmount) {
                        return Mono.error(new BadRequestException("El monto de pago mensual debe ser: S/. " + monthlyPaymentAmount));
                    }

                    CreditPayment mappedCredit = transactionMapper.toDocument(request);
                    mappedCredit.setInterest(interest);

                    return transactionRepository.save(mappedCredit).then(Mono.just(credit));
                }).zipWith(numberOfPaymentsMade).flatMap(
                        tuple -> {
                            Credit credit = tuple.getT1();
                            int paymentsMade = (int) (long) tuple.getT2();
                            credit.setStatus(credit.checkStatus(paymentsMade));
                            return creditRepository.save(credit).then();
                        }
                );
    }
}
