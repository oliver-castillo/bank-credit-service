package com.bank.creditservice.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CreditCardRequest {
    @NotNull
    private String clientId;

    @NotNull
    private Double creditLimit;
}
