package com.bank.creditservice.mapper;

import com.bank.creditservice.model.document.Charge;
import com.bank.creditservice.model.document.CreditCardPayment;
import com.bank.creditservice.model.document.CreditPayment;
import com.bank.creditservice.model.document.Transaction;
import com.bank.creditservice.model.dto.request.ChargeRequest;
import com.bank.creditservice.model.dto.request.CreditCardPaymentRequest;
import com.bank.creditservice.model.dto.request.CreditPaymentRequest;
import com.bank.creditservice.model.dto.response.TransactionResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    CreditPayment toDocument(CreditPaymentRequest request);

    CreditCardPayment toDocument(CreditCardPaymentRequest request);

    Charge toDocument(ChargeRequest request);

    TransactionResponse toResponse(Transaction document);
}
