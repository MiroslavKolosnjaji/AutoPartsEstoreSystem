package com.myproject.autopartsestoresystem.controller.global;

import com.myproject.autopartsestoresystem.exception.CustomerDoesntExistsException;
import com.myproject.autopartsestoresystem.exception.EmailAddressAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author Miroslav Kološnjaji
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAddressAlreadyExistsException.class)
    public ResponseEntity<String> handlEmailAddressAlreadyExistsException(EmailAddressAlreadyExistsException e) {
        return new ResponseEntity<>("Email address already exists", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CustomerDoesntExistsException.class)
    public ResponseEntity<String> handlCustomerDoesntExistsException(CustomerDoesntExistsException e) {
        return new ResponseEntity<>("Customer doesn't exists", HttpStatus.NOT_FOUND);
    }
}
