package com.example.demo.controllers;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import com.braintreegateway.WebhookNotification;
import com.braintreegateway.WebhookNotificationGateway;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Map;

@RestController
public class MainController {

    private final static Logger log = LoggerFactory.getLogger(MainController.class);
    BraintreeGateway gateway;

    @PostConstruct
    void init() {
        /* TODO Move these to a secure place - ENV or vault */
         gateway = new BraintreeGateway(
                Environment.SANDBOX,
                "kb5t3bztdttf2jfb",
                "jtx2r85k9849x6b8",
                "hidden"
        );
    }

    @GetMapping("/")
    public String hello() {
        return "hello world!";
    }

    @RequestMapping(value = "/payload",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String getPayloadFromBraintreeWebhook(@RequestParam Map<String, String> request) {
        /*
        When webhooks are triggered in the gateway, a notification is sent as a POST request to the specified destination URL. The post body contains two x-www-form-urlencoded parameters:
            bt_signature
            bt_payload
         */
        System.out.println("Webhook event received " + request.get("bt_signature"));
        System.out.println(request.get("bt_payload"));

        WebhookNotification webhookNotification = gateway.webhookNotification().parse(
                request.get("bt_signature"),
                request.get("bt_payload")
        );
        System.out.println("Notification of kind: "+ webhookNotification.getKind() + " received");
        return webhookNotification.getSourceMerchantId();
    }
}
