package com.jenkins.test.service;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TestService {
    
    public String test() {
        log.info("TestService test method called");
        return "Service is working lol!";
    }
}
