package com.example.actuator.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class AlertWebhookPayload {

    // Alertmanager v2 webhook 공통 필드들
    private String version;                // e.g. "4"
    private String status;                 // "firing" | "resolved"
    private String receiver;               // 라우팅된 receiver 이름
    private String groupKey;               // 그룹 키
    private Map<String, String> groupLabels;
    private Map<String, String> commonLabels;
    private Map<String, String> commonAnnotations;
    private String externalURL;

    @JsonProperty("alerts")
    private List<AlertEntry> alerts;       // 개별 알림 목록

    private Integer truncatedAlerts;       // 일부 버전에서 제공

    // 편의 메서드(있으면 코드가 깔끔해집니다)
    public String getCommonAlertName() {
        return commonLabels != null ? commonLabels.get("alertname") : null;
    }
    public String getCommonInstance() {
        return commonLabels != null ? commonLabels.get("instance") : null;
    }
    public String getCommonJob() {
        return commonLabels != null ? commonLabels.get("job") : null;
    }
    public String getCommonSeverity() {
        return commonLabels != null ? commonLabels.get("severity") : null;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter @Setter
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    @ToString
    public static class AlertEntry {
        private String status;                   // "firing" | "resolved"
        private Map<String, String> labels;      // alertname, instance, job, severity 등
        private Map<String, String> annotations; // summary, description, runbook_url 등
        private OffsetDateTime startsAt;
        private OffsetDateTime endsAt;

        private String generatorURL;
        private String fingerprint;

        // AM 0.27+ 일부 구현체가 내려주는 편의 링크들 (없을 수도 있음)
        private String silenceURL;
        private String dashboardURL;
        private String panelURL;

        // AM가 템플릿에서 {{ $value }} 를 담아 보내는 구현도 있어 문자열로 받는 게 안전
        private String value;

        // 편의 getter
        public String getAlertName()     { return labels != null ? labels.get("alertname") : null; }
        public String getInstance()      { return labels != null ? labels.get("instance") : null; }
        public String getJob()           { return labels != null ? labels.get("job") : null; }
        public String getSeverity()      { return labels != null ? labels.get("severity") : null; }
        public String getSummary()       { return annotations != null ? annotations.get("summary") : null; }
        public String getDescription()   { return annotations != null ? annotations.get("description") : null; }
    }
}