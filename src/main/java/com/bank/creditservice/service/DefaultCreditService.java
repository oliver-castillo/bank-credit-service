package com.bank.creditservice.service;

import com.bank.creditservice.exception.AlreadyExistsException;
import com.bank.creditservice.exception.NotFoundException;
import com.bank.creditservice.mapper.CreditMapper;
import com.bank.creditservice.model.dto.request.CreditRequest;
import com.bank.creditservice.model.dto.response.CreditResponse;
import com.bank.creditservice.model.dto.response.OperationResponse;
import com.bank.creditservice.model.enums.ClientType;
import com.bank.creditservice.model.enums.CreditStatus;
import com.bank.creditservice.repository.CreditRepository;
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
public class DefaultCreditService implements CreditService {
    private final CreditRepository repository;
    private final CreditMapper mapper;

    @Override
    public Mono<OperationResponse> save(CreditRequest request) {
        return validateRequest(request)
                .flatMap(valid -> repository.save(mapper.toDocument(request))
                        .map(document -> new OperationResponse(
                                ResponseMessage.CREATED_SUCCESSFULLY,
                                HttpStatus.CREATED)));
    }

    @Override
    public Mono<CreditResponse> findById(String id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .switchIfEmpty(Mono.error(new NotFoundException("No se encontró el registro")));
    }

    @Override
    public Flux<CreditResponse> findByClientId(String clientId) {
        return repository.findByClientId(clientId)
                .map(mapper::toResponse)
                .switchIfEmpty(Flux.error(new NotFoundException("No se encontraron registros")));
    }

    @Override
    public Flux<CreditResponse> findAll() {
        return repository.findAll()
                .map(mapper::toResponse)
                .switchIfEmpty(Flux.error(new NotFoundException("No se encontraron registros")));
    }

    private Mono<Boolean> validateRequest(CreditRequest request) {
        if (request.getClientType() == ClientType.BUSINESS) {
            return Mono.just(true);
        } else {
            return repository.findByClientId(request.getClientId())
                    .filter(credit -> credit.getClientType() == ClientType.PERSONAL
                            && credit.getStatus() == CreditStatus.ACTIVE)
                    .count()
                    .handle((count, sink) -> {
                        if (count > 0) {
                            sink.error(new AlreadyExistsException("Un cliente personal solo puede tener un crédito"));
                        } else {
                            sink.next(true);
                        }
                    });
        }
    }
}
