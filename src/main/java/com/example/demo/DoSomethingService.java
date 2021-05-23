package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DoSomethingService {
    public void logSomething(){
        log.info("Inside DoSomethingService");
    }
}
