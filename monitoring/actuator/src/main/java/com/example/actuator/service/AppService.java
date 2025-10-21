package com.example.actuator.service;

import com.example.actuator.api.JandiApiClient;
import com.example.actuator.dto.AlertDto;
import com.example.actuator.dto.JandiDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppService {

    private final JandiApiClient jandiApiClient;

    public void promAlert(AlertDto alertDto) {
        jandiApiClient.send(new JandiDto(alertDto));
    }
}
