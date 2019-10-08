package com.example.demo.service;

import com.braintreegateway.*;
import com.example.demo.config.GatewayProvider;
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

    @Autowired
    GatewayProvider gatewayProvider;

    public String processBrainTreeEvent(Map<String, String> request) {
        System.out.println("Webhook event received " + request.get("bt_signature"));

        WebhookNotification webhookNotification = gatewayProvider.getGateway().webhookNotification().parse(
                request.get("bt_signature"),
                request.get("bt_payload")
        );

        System.out.println("Notification of kind: " + webhookNotification.getKind() + " received");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println("Notification payload : " + gson.toJson(webhookNotification));

        try {
            messagingService.postMessage(webhookNotification);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

        return webhookNotification.getSourceMerchantId();
    }
}
