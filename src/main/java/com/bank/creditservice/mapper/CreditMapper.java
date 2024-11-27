package com.bank.creditservice.mapper;

import com.bank.creditservice.model.document.Credit;
import com.bank.creditservice.model.dto.request.CreditRequest;
import com.bank.creditservice.model.dto.response.CreditResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreditMapper {
    Credit toDocument(CreditRequest request);

    CreditResponse toResponse(Credit document);
}
