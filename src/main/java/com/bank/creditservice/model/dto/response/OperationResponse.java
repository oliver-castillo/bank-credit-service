package com.bank.creditservice.model.dto.response;

import com.bank.creditservice.util.ResponseMessage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class OperationResponse {
    private String message;
    private String status;
    private Integer code;
    private LocalDateTime timestamp;

    public OperationResponse(ResponseMessage message, HttpStatus httpStatus) {
        this.message = message.getMessage();
        this.status = httpStatus.getReasonPhrase();
        this.code = httpStatus.value();
        this.timestamp = LocalDateTime.now();
    }

    public OperationResponse(String message, HttpStatus httpStatus) {
        this.message = message;
        this.status = httpStatus.getReasonPhrase();
        this.code = httpStatus.value();
        this.timestamp = LocalDateTime.now();
    }
}
