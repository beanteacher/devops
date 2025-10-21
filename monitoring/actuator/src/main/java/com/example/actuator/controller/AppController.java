package com.example.actuator.controller;

import com.example.actuator.dto.AlertDto;
import com.example.actuator.dto.AlertWebhookPayload;
import com.example.actuator.service.AppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class AppController {

    private final AppService appService;

    @PostMapping(value = "/prom-alert", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> promAlert(@RequestBody AlertWebhookPayload payload) {
        appService.promAlert(new AlertDto(payload));
        return ResponseEntity.ok().build(); // null 반환 금지
    }
}
