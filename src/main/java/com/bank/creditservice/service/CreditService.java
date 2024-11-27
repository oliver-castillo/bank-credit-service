package com.bank.creditservice.service;

import com.bank.creditservice.model.dto.request.CreditRequest;
import com.bank.creditservice.model.dto.response.CreditResponse;
import com.bank.creditservice.model.dto.response.OperationResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditService {
    Mono<OperationResponse> save(CreditRequest request);

    Mono<CreditResponse> findById(String id);

    Flux<CreditResponse> findByClientId(String clientId);

    Flux<CreditResponse> findAll();
}
