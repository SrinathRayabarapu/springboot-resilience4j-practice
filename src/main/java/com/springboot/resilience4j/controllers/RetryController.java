package com.springboot.resilience4j.controllers;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Log4j2
@RestController
public class RetryController {

    RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/getInvoice")
    @Retry(name = "getInvoiceRetry", fallbackMethod = "getInvoiceFallback")
    public String getInvoice() {
        log.info("getInvoice() call starts here");
        // this url available in "spring-boot-security" repo
        ResponseEntity<String> entity = restTemplate.getForEntity("http://localhost:8081/user", String.class);
        log.info("Response :" + entity.getStatusCode());
        return entity.getBody();
    }

    public String getInvoiceFallback(Exception e) {
        log.info("---RESPONSE FROM FALLBACK METHOD--- {}", e.getMessage());
        return "SERVICE IS DOWN, PLEASE TRY AFTER SOMETIME !!!";
    }

}
