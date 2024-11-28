package com.bank.creditservice.service;

import com.bank.creditservice.model.dto.request.TransactionRequest;
import com.bank.creditservice.model.dto.response.OperationResponse;
import reactor.core.publisher.Mono;

public interface TransactionService<T extends TransactionRequest> {
    Mono<OperationResponse> makeTransaction(T request);
}
