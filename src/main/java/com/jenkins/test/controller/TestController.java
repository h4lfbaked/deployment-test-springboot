package com.jenkins.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jenkins.test.service.TestService;

@RestController
@RequestMapping("/api")
public class TestController {
    
    @Autowired
    private TestService testService;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        String result = testService.test();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/testPalaBapakKau")
    public ResponseEntity<String> testPalaBapakKau() {
        String result = testService.testPalaBapakKau();
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Application is running!");
    }

}
