package com.pfms.walletCategory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class WalletCategoryExceptionHandler {

    @ExceptionHandler(value = WalletCategoryException.class)
    public ResponseEntity<Object> handleWalletException(WalletCategoryException e) {
        WalletCategoryError error = new WalletCategoryError(e.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }
}
