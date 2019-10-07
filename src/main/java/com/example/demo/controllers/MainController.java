package com.example.demo.controllers;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import com.braintreegateway.WebhookNotification;
import com.example.demo.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Map;

@RestController
public class MainController {

    private final static Logger log = LoggerFactory.getLogger(MainController.class);

    @Autowired
    TransactionService transactionService;

    @GetMapping("/")
    public String hello() {
        return "hello world!";
    }

    @RequestMapping(value = "/payload",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String getPayloadFromBraintreeWebhook(@RequestParam Map<String, String> request) {

        return transactionService.processBrainTreeEvent(request);

    }
}
