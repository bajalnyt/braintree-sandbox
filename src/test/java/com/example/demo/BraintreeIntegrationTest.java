package com.example.demo;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import com.braintreegateway.WebhookNotification;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;

import java.util.HashMap;

public class BraintreeIntegrationTest {


    @Test
    public void sendSamplePayload() {
        BraintreeGateway gateway = new BraintreeGateway(
                Environment.SANDBOX,
                "kb5t3bztdttf2jfb",
                "secret",
                "secret"
        );
        HashMap<String, String> sampleNotification = gateway.webhookTesting().sampleNotification(
                WebhookNotification.Kind.SUBSCRIPTION_CANCELED, "my_integration_test"
        );

        WebhookNotification webhookNotification = gateway.webhookNotification().parse(
                sampleNotification.get("bt_signature"),
                sampleNotification.get("bt_payload")
        );

        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(webhookNotification));
    }
}
