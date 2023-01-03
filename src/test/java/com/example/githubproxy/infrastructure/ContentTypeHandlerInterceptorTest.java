package com.example.githubproxy.infrastructure;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContentTypeHandlerInterceptorTest {
    private final ContentTypeHandlerInterceptor contentTypeHandlerInterceptor = new ContentTypeHandlerInterceptor();

    @Mock
    private HttpServletRequest request;

    @Test
    void shouldThrowContentTypeNotSupportedException() {
        when(request.getHeader(HttpHeaders.CONTENT_TYPE))
                .thenReturn("application/xml");
        assertThrows(ContentTypeNotSupportedException.class, () -> contentTypeHandlerInterceptor.preHandle(request, null, null));
    }

    @Test
    void shouldReturnTrueForJsonContentType() {
        when(request.getHeader(HttpHeaders.CONTENT_TYPE))
                .thenReturn("application/json");
        assertTrue(contentTypeHandlerInterceptor.preHandle(request, null, null));
    }
}