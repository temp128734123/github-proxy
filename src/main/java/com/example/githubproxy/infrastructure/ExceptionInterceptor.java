package com.example.githubproxy.infrastructure;

import com.example.githubproxy.contract.ApplicationExceptionDto;
import com.example.githubproxy.entrypoint.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionInterceptor extends ResponseEntityExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public final ResponseEntity<ApplicationExceptionDto> handleUserNotFound(UserNotFoundException ex) {
        log.warn("User was not found: ", ex);
        return new ResponseEntity<>(new ApplicationExceptionDto(HttpStatus.NOT_FOUND.value(), ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ContentTypeNotSupportedException.class)
    @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE)
    public final ResponseEntity<ApplicationExceptionDto> handleContentTypeNotSupported(ContentTypeNotSupportedException ex) {
        log.warn("Content type not supported: ", ex);
        return new ResponseEntity<>(new ApplicationExceptionDto(HttpStatus.NOT_ACCEPTABLE.value(), ex.getMessage()), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ApplicationExceptionDto> handleAllExceptions(Exception ex) {
        log.warn("Error occurred: ", ex);
        return new ResponseEntity<>(new ApplicationExceptionDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
