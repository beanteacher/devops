package com.example.actuator.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AlertDto {
    private String label;
    private String instance;
    private String job;
    private String description;
    private String severity;
    private List<AlertWebhookPayload.AlertEntry> alerts;

    public AlertDto (AlertWebhookPayload payload) {
        this.label = payload.getCommonLabels().get("alertname");
        this.instance = payload.getCommonLabels().get("instance");
        this.job = payload.getCommonLabels().get("job");
        this.description = payload.getCommonAnnotations().get("description");
        this.severity = payload.getCommonLabels().get("severity");
        this.alerts = payload.getAlerts();
    }
}
