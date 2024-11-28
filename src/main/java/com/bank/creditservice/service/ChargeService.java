package com.bank.creditservice.service;

import com.bank.creditservice.model.dto.request.ChargeRequest;
import com.bank.creditservice.model.dto.response.OperationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ChargeService implements TransactionService<ChargeRequest> {
    @Override
    public Mono<OperationResponse> makeTransaction(ChargeRequest request) {
        return null;
    }
}
