package com.example.actuator.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class JandiDto {
    private String body;
    private String connectColor;
    private List<ConnectInfo> connectInfo;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ConnectInfo {
        private String title;
        private String description;

        public ConnectInfo(String title, String description) {
            this.title = title;
            this.description = description;
        }
    }

    public JandiDto(AlertDto alert) {
        this.body = "[" + alert.getSeverity().toUpperCase() + "]" + alert.getLabel();
        this.connectColor = "#FAC11B";
        this.connectInfo = new ArrayList<>();
        this.connectInfo.add(new ConnectInfo("[" + alert.getSeverity().toUpperCase() + "]" + alert.getLabel(), alert.getDescription() + alert.getAlerts().get(0).getStartsAt()));
    }
}

