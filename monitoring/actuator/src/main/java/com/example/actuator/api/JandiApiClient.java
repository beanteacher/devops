package com.example.actuator.api;

import com.example.actuator.api.config.JandiApiRequestInterceptor;
import com.example.actuator.api.config.JandiFeignConfig;
import com.example.actuator.dto.JandiDto;
import com.example.actuator.api.response.ApiResponses;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "jandiApiClient", url = "https://wh.jandi.com/connect-api/webhook/33170375/d4f3ee370ab098ff14595f7c76bff911", configuration = {JandiApiRequestInterceptor.class, JandiFeignConfig.class})
public interface JandiApiClient {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    ApiResponses send(@RequestBody JandiDto reqData);
}
