package com.example.githubproxy.infrastructure;

import com.example.githubproxy.contract.ApplicationExceptionDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionInterceptorTest {
    private final ExceptionInterceptor exceptionInterceptor = new ExceptionInterceptor();

    @Test
    void shouldHandleUserNotFoundException() {
        ResponseEntity<ApplicationExceptionDto> responseEntity = exceptionInterceptor.handleUserNotFound(new UserNotFoundException());

        assertEquals(404, responseEntity.getStatusCode().value());
        assertEquals(responseEntity.getBody(), new ApplicationExceptionDto(404, "Username not found"));
    }

    @Test
    void shouldHandleContentTypeNotSupported() {
        ResponseEntity<ApplicationExceptionDto> responseEntity = exceptionInterceptor.handleContentTypeNotSupported(new ContentTypeNotSupportedException());

        assertEquals(406, responseEntity.getStatusCode().value());
        assertEquals(responseEntity.getBody(), new ApplicationExceptionDto(406, "Content type not supported"));
    }

    @Test
    void shouldHandleAllExceptions() {
        ResponseEntity<ApplicationExceptionDto> responseEntity = exceptionInterceptor.handleAllExceptions(new Exception("error"));

        assertEquals(500, responseEntity.getStatusCode().value());
        assertEquals(responseEntity.getBody(), new ApplicationExceptionDto(500, "error"));
    }
}