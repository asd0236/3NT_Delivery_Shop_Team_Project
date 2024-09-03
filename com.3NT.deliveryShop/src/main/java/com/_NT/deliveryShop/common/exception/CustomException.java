package com._NT.deliveryShop.common.exception;

import com._NT.deliveryShop.common.codes.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class CustomException extends ResponseStatusException {
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(HttpStatusCode.valueOf(errorCode.getStatus()));
        this.errorCode = errorCode;
    }

    public CustomException(ErrorCode errorCode, String message) {
        super(HttpStatusCode.valueOf(errorCode.getStatus()), message);
        this.errorCode = errorCode;
    }
}
