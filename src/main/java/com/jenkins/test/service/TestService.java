package com.jenkins.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sriks.boost.TestPalaBapakKau;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TestService {

    @Autowired
    private TestPalaBapakKau testPalaBapakKau;

    public String test() {
        log.info("TestService test method called");
        return "Service is working lol!";
    }

    public String testPalaBapakKau() {
        log.info("TestService testPalaBapakKau method called");
        return testPalaBapakKau.testMethod();
    }

}
