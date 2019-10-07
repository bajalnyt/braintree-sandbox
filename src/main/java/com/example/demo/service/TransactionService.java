package com.example.demo.service;

import com.braintreegateway.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

@Service
public class TransactionService {

    @Autowired
    MessagingService messagingService;

    BraintreeGateway gateway;

    @PostConstruct
    void init() {
        /* TODO Move these to a secure place - ENV or vault */
        gateway = new BraintreeGateway(
                Environment.SANDBOX,
                "kb5t3bztdttf2jfb",
                "jtx2r85k9849x6b8",
                "secret"
        );
    }

    void createCustomerWithAddress() {
        CustomerRequest request = new CustomerRequest()
                .firstName("Mark")
                .lastName("Jones")
                .company("Jones Co.")
                .email("mark.jones@example.com")
                .fax("419-555-1234")
                .phone("614-555-1234")
                .website("http://example.com")
                ;
        Result<Customer> result = gateway.customer().create(request);

    }

    public String processBrainTreeEvent(Map<String, String> request) {
        System.out.println("Webhook event received " + request.get("bt_signature"));
        System.out.println(request.get("bt_payload"));

        WebhookNotification webhookNotification = gateway.webhookNotification().parse(
                request.get("bt_signature"),
                request.get("bt_payload")
        );

        System.out.println("Notification of kind: "+ webhookNotification.getKind() + " received");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println("Notification payload : " + gson.toJson(webhookNotification));
        // Push event payload to Queue:
        try {
            messagingService.postMessage(webhookNotification);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return webhookNotification.getSourceMerchantId();
    }
}
