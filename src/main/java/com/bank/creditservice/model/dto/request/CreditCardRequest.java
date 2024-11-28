package com.bank.creditservice.model.dto.request;

import com.bank.creditservice.model.enums.ClientType;
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

    private ClientType clientType;
}
