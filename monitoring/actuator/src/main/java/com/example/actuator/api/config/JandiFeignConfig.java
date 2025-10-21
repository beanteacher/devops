package com.example.actuator.api.config;

import feign.Logger;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.JsonFormWriter;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ScopeMetadata;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@EnableFeignClients(basePackages = "com.example.actuator")
@Configuration
@RequiredArgsConstructor
@Import(JsonFormWriter.class)
public class JandiFeignConfig {
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL; // 요청/응답 로그 전체 출력
    }

    @Bean
    public Decoder feignDecoder() {
        return new ResponseEntityDecoder(new SpringDecoder(HttpMessageConverters::new));
    }

    @Bean
    public Encoder feignEncoder(JsonFormWriter jsonFormWriter) {
        return new SpringFormEncoder(new SpringEncoder(() -> new HttpMessageConverters(new MappingJackson2HttpMessageConverter())));
    }
}
