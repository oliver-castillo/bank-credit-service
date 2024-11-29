package com.bank.creditservice.service.implementation;

import com.bank.creditservice.exception.NotFoundException;
import com.bank.creditservice.mapper.CreditCardMapper;
import com.bank.creditservice.model.dto.request.CreditCardRequest;
import com.bank.creditservice.model.dto.response.CreditCardResponse;
import com.bank.creditservice.model.dto.response.OperationResponse;
import com.bank.creditservice.repository.CreditCardRepository;
import com.bank.creditservice.service.CreditCardService;
import com.bank.creditservice.util.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class DefaultCreditCardService implements CreditCardService {
    private final CreditCardRepository repository;
    private final CreditCardMapper mapper;

    @Override
    public Mono<OperationResponse> save(CreditCardRequest request) {
        return repository.save(mapper.toDocument(request))
                .map(document -> new OperationResponse(
                        ResponseMessage.CREATED_SUCCESSFULLY,
                        HttpStatus.CREATED));
    }

    @Override
    public Mono<CreditCardResponse> findByCardNumber(String cardNumber) {
        return repository.findCreditCardByCardNumber(cardNumber)
                .map(mapper::toResponse)
                .switchIfEmpty(Mono.error(new NotFoundException("No se encontró la tarjeta de crédito")));
    }

    @Override
    public Flux<CreditCardResponse> findAll() {
        return repository.findAll()
                .map(mapper::toResponse);
    }
}
