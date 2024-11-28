package com.bank.creditservice.model.enums;

import com.bank.creditservice.exception.BadRequestException;

public enum ClientType {
    PERSONAL, BUSINESS;

    public static ClientType fromString(String clientType) {
        if (clientType.equals("personal")) {
            return ClientType.PERSONAL;
        } else if (clientType.equals("business")) {
            return ClientType.BUSINESS;
        } else {
            throw new BadRequestException("El tipo de cliente no es válido");
        }
    }
}
