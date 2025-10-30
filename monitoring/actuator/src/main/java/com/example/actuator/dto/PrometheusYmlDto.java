package com.example.actuator.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PrometheusYmlDto {
    private Global global;
    private List<Job> jobs;
    private Alertmanagers alertmanagers;
    private List<String> rules;

    @Builder
    public PrometheusYmlDto(Global global, List<Job> jobs, Alertmanagers alertmanagers, List<String> rules) {
        this.global = global;
        this.jobs = jobs;
        this.alertmanagers = alertmanagers;
        this.rules = rules;
    }

    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    public static class Global {
        // getters/setters
        private String evaluationInterval; // e.g., "15s"
        private String scrapeInterval;     // e.g., "15s"

        @Builder
        public Global(String evaluationInterval, String scrapeInterval) {
            this.evaluationInterval = evaluationInterval;
            this.scrapeInterval = scrapeInterval;
        }
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    public static class Job {
        private String jobName;         // ex) "windows"
        private String metricsPath;     // null 가능
        private String targetsIpPort;   // ex) "host.docker.internal:9182"
        private boolean hasLabels;      // labels 존재 여부 플래그
        private List<Label> labels;     // null/empty 가능

        @Builder
        public Job(String jobName, String metricsPath, String targetsIpPort, boolean hasLabels, List<Label> labels) {
            this.jobName = jobName;
            this.metricsPath = metricsPath;
            this.targetsIpPort = targetsIpPort;
            this.hasLabels = hasLabels;
            this.labels = labels;
        }
    }

    @Getter
    @ToString
    @Setter
    @NoArgsConstructor
    public static class Alertmanagers {
        private String targetsIpPort; // ex) "host.docker.internal:9093"

        @Builder
        public Alertmanagers(String targetsIpPort) {
            this.targetsIpPort = targetsIpPort;
        }
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    public static class Label {
        private String key;
        private String value;

        @Builder
        public Label(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }
}
