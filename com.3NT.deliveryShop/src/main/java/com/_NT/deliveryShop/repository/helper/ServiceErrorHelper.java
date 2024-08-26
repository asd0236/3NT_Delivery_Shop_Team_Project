package com._NT.deliveryShop.repository.helper;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ServiceErrorHelper {

    public ResponseStatusException notFound(String reason) {
        return new ResponseStatusException(NOT_FOUND, reason);
    }

    public ResponseStatusException badRequest(String reason) {
        return new ResponseStatusException(BAD_REQUEST, reason);
    }

    public ResponseStatusException unauthorized(String reason) {
        return new ResponseStatusException(UNAUTHORIZED, reason);
    }

    public ResponseStatusException forbidden(String reason) {
        return new ResponseStatusException(FORBIDDEN, reason);
    }
}
