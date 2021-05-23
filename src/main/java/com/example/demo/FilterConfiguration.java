package com.example.demo;

import brave.baggage.BaggageField;
import brave.baggage.CorrelationScopeConfig;
import brave.baggage.CorrelationScopeCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfiguration {

    @Autowired
    private DoSomethingService service;

    @Bean
    public BaggageField randomField(){
        return BaggageField.create("session-id");
    }

    @Bean
    CorrelationScopeCustomizer flushBusinessProcessToMDCOnUpdate() {
    return b -> b.add(
            CorrelationScopeConfig.SingleCorrelationField.newBuilder(randomField()).flushOnUpdate().build()
    );
    }

    @Bean
    public GlobalFilter customFilter() {
        return new CustomGlobalFilter(randomField(), service);
    }
}
