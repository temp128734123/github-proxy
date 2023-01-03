package com.example.githubproxy.infrastructure;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ContentTypeHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Object handler) {
        String contentType = request.getHeader(HttpHeaders.CONTENT_TYPE);
        if (contentType != null && !contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
            throw new ContentTypeNotSupportedException();
        }
        return true;
    }
}
