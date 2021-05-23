package com.example.demo;

import brave.baggage.BaggageField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Slf4j
public class CustomGlobalFilter implements GlobalFilter {

    private BaggageField sessionIdField;

    private DoSomethingService service;

    public CustomGlobalFilter(BaggageField sessionIdField, DoSomethingService service) {
        this.sessionIdField = sessionIdField;
        this.service = service;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        List<HttpCookie> sessionIdCookies = exchange.getRequest().getCookies().get(sessionIdField.name());
        String sessionId;
        if(CollectionUtils.isEmpty(sessionIdCookies))
        {
            sessionId = UUID.randomUUID().toString();
            exchange.getResponse().addCookie(ResponseCookie.from("SESSION_ID", sessionId).build());
        }
        else {
            sessionId = sessionIdCookies.get(0).getValue();
        }
        sessionIdField.updateValue(sessionId);
        log.info("Inside CustomGlobalFilter");
        service.logSomething();

        return chain.filter(exchange.mutate().response(exchange.getResponse()).build());
    }
}
