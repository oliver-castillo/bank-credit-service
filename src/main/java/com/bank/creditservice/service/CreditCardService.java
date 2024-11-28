package com.bank.creditservice.service;

import com.bank.creditservice.model.dto.request.CreditCardRequest;
import com.bank.creditservice.model.dto.response.CreditCardResponse;
import com.bank.creditservice.model.dto.response.OperationResponse;
import reactor.core.publisher.Mono;

public interface CreditCardService {
    Mono<OperationResponse> save(CreditCardRequest request);

    Mono<CreditCardResponse> findById(String id);
}
