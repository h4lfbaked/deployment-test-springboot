package com.jenkins.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {"com.jenkins.test", "com.jenkins.uat", "com.sriks.boost"})
@ComponentScan(basePackages = {
    "com.jenkins.test",
    "com.sriks.boost"   
})
public class TestApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestApplication.class, args);
	}

}
