package com.example.githubproxy.infrastructure;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class FeignErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new Default();
    @Override
    public Exception decode(String methodKey, Response response) {
        if (methodKey.equals("GithubClient#getRepositories(String)") && response.status() == 404) {
            return new UserNotFoundException();
        }
        return defaultErrorDecoder.decode(methodKey, response);
    }
}
