package co.com.crediya.security.jwt.filter;

import co.com.crediya.model.exceptions.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;

import static org.mockito.Mockito.*;

class JwtFilterTest {

    private JwtFilter jwtFilter;
    private ServerWebExchange exchange;
    private ServerHttpRequest request;
    private WebFilterChain chain;

    @BeforeEach
    void setUp() {
        jwtFilter = new JwtFilter();
        exchange = mock(ServerWebExchange.class);
        request = mock(ServerHttpRequest.class);
        chain = mock(WebFilterChain.class);

        when(exchange.getRequest()).thenReturn(request);
        when(chain.filter(exchange)).thenReturn(Mono.empty());

        when(exchange.getAttributes()).thenReturn(new HashMap<>());

    }

    @Test
    void shouldPassThroughForAuthPaths() {
        RequestPath path = mock(RequestPath.class);
        when(request.getPath()).thenReturn(path);
        when(path.value()).thenReturn("/auth/login");

        StepVerifier.create(jwtFilter.filter(exchange, chain))
                .verifyComplete();

        verify(chain).filter(exchange);
    }

    @Test
    void shouldThrowExceptionWhenNoAuthorizationHeader() {
        RequestPath path = mock(RequestPath.class);
        when(request.getPath()).thenReturn(path);
        when(path.value()).thenReturn("/api/test");
        when(request.getHeaders()).thenReturn(HttpHeaders.EMPTY);

        StepVerifier.create(jwtFilter.filter(exchange, chain))
                .expectErrorMatches(throwable ->
                        throwable instanceof JwtException &&
                                ((JwtException) throwable).getMessage().equals(JwtException.TOKEN_NOT_FOUND))
                .verify();

        verify(chain, never()).filter(exchange);
    }

    @Test
    void shouldThrowExceptionWhenAuthorizationHeaderInvalid() {
        RequestPath path = mock(RequestPath.class);
        when(request.getPath()).thenReturn(path);
        when(path.value()).thenReturn("/api/test");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "InvalidToken");
        when(request.getHeaders()).thenReturn(headers);

        StepVerifier.create(jwtFilter.filter(exchange, chain))
                .expectErrorMatches(throwable ->
                        throwable instanceof JwtException &&
                                ((JwtException) throwable).getMessage().equals(JwtException.INVALID_TOKEN))
                .verify();

        verify(chain, never()).filter(exchange);
    }


    @Test
    void shouldPassWithValidBearerToken() {
        RequestPath path = mock(RequestPath.class);
        when(request.getPath()).thenReturn(path);
        when(path.value()).thenReturn("/api/test");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer valid-token");
        when(request.getHeaders()).thenReturn(headers);

        StepVerifier.create(jwtFilter.filter(exchange, chain))
                .verifyComplete();

        verify(chain).filter(exchange);
        assert exchange.getAttributes().get("token").equals("valid-token");
    }
}

