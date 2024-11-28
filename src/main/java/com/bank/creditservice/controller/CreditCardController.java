package com.bank.creditservice.controller;

import com.bank.creditservice.model.dto.request.CreditCardRequest;
import com.bank.creditservice.model.dto.response.CreditCardResponse;
import com.bank.creditservice.model.dto.response.OperationResponse;
import com.bank.creditservice.service.CreditCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class CreditCardController {
    private final CreditCardService creditCardService;

    @PostMapping(value = "credit-card")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<OperationResponse> save(@RequestBody @Valid CreditCardRequest request) {
        return creditCardService.save(request);
    }

    @GetMapping(value = "credit-card/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<CreditCardResponse> findById(@PathVariable String id) {
        return creditCardService.findById(id);
    }
}
