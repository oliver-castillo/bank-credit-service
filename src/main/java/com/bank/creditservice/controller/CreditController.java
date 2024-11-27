package com.bank.creditservice.controller;

import com.bank.creditservice.model.dto.request.CreditRequest;
import com.bank.creditservice.model.dto.response.CreditResponse;
import com.bank.creditservice.model.dto.response.OperationResponse;
import com.bank.creditservice.model.enums.ClientType;
import com.bank.creditservice.service.CreditService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class CreditController {
    private final CreditService creditService;

    @PostMapping(value = "{clientType}")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<OperationResponse> save(@RequestBody @Valid CreditRequest request, @PathVariable String clientType) {
        request.setClientType(clientType.equals("personal") ? ClientType.PERSONAL : ClientType.BUSINESS);
        return creditService.save(request);
    }

    @GetMapping(value = "client/{clientId}")
    @ResponseStatus(HttpStatus.OK)
    public Flux<CreditResponse> findByClientId(@PathVariable String clientId) {
        return creditService.findByClientId(clientId);
    }

    @GetMapping(value = "{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<CreditResponse> findById(@PathVariable String id) {
        return creditService.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<CreditResponse> findAll() {
        return creditService.findAll();
    }

}
