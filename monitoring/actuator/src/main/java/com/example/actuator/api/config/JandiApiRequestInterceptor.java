package com.example.actuator.api.config;


import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;

public class JandiApiRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        template.header("Accept", "application/vnd.tosslab.jandi-v2+json");
    }
}
