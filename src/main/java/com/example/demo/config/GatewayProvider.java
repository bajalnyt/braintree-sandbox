package com.example.demo.config;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class GatewayProvider {

    BraintreeGateway gateway;

    @PostConstruct
    void init() {

        gateway = new BraintreeGateway(
                Environment.SANDBOX,
                "kb5t3bztdttf2jfb",
                "jtx2r85k9849x6b8",
                ""
        );
    }

    public BraintreeGateway getGateway() {
        return gateway;
    }
}
