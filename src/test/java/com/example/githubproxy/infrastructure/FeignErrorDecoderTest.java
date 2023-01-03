package com.example.githubproxy.infrastructure;

import feign.FeignException;
import feign.Request;
import feign.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@ExtendWith(MockitoExtension.class)
class FeignErrorDecoderTest {
    private final FeignErrorDecoder feignErrorDecoder = new FeignErrorDecoder();

    @Test
    void shouldThrowUserNotFoundExceptionWhenGithubClientGetRepositoriesReturned404() {
        Response response = buildMockResponse(404);

        assertInstanceOf(UserNotFoundException.class, feignErrorDecoder.decode("GithubClient#getRepositories(String)", response));
    }

    @Test
    void shouldThrowDefaultExceptionWhenGithubClientGetRepositoriesReturnedUnexpectedError() {
        Response response = buildMockResponse(418);

        assertInstanceOf(FeignException.class, feignErrorDecoder.decode("GithubClient#getRepositories(String)", response));
    }

    @Test
    void shouldThrowDefaultExceptionWhenUnhandledMethodReturnedError() {
        Response response = buildMockResponse(404);

        assertInstanceOf(FeignException.class, feignErrorDecoder.decode("Xxx#xxx()", response));
    }

    private static Response buildMockResponse(int status) {
        Response.Builder builder = Response.builder();
        builder.status(status);
        builder.request(Request.create(Request.HttpMethod.GET, "/", new HashMap<>(), new byte[]{}, null, null));
        return builder.build();
    }
}