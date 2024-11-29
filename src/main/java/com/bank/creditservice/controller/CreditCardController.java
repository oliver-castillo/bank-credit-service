package com.bank.creditservice.controller;

import com.bank.creditservice.model.dto.request.CreditCardRequest;
import com.bank.creditservice.model.dto.response.CreditCardResponse;
import com.bank.creditservice.model.dto.response.OperationResponse;
import com.bank.creditservice.service.CreditCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "credit-cards")
public class CreditCardController {
    private final CreditCardService creditCardService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<OperationResponse> save(@RequestBody @Valid CreditCardRequest request) {
        return creditCardService.save(request);
    }

    @GetMapping(value = "{cardNumber}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<CreditCardResponse> findByCardNumber(@PathVariable String cardNumber) {
        return creditCardService.findByCardNumber(cardNumber);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<CreditCardResponse> findAll() {
        return creditCardService.findAll();
    }
}
