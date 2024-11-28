package com.bank.creditservice.mapper;

import com.bank.creditservice.model.document.CreditCard;
import com.bank.creditservice.model.dto.request.CreditCardRequest;
import com.bank.creditservice.model.dto.response.CreditCardResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreditCardMapper {
    CreditCard toDocument(CreditCardRequest request);

    CreditCardResponse toResponse(CreditCard document);
}
