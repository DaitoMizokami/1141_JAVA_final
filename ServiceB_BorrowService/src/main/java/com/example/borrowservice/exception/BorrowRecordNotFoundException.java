package com.example.borrowservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BorrowRecordNotFoundException extends RuntimeException {
    public BorrowRecordNotFoundException(String message) {
        super(message);
    }
}
