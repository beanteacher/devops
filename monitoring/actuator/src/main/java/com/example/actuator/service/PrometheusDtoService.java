package com.example.actuator.service;

import com.example.actuator.dto.PrometheusYmlDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
@Slf4j
public class PrometheusDtoService {

    private static final String PROM_TEMPLATE =
"""
global:
  evaluation_interval: {{global.evaluationInterval}}
  scrape_interval: {{global.scrapeInterval}}

scrape_configs:
  {{#jobs}}
  - job_name: '{{jobName}}'
    {{#metricsPath}}metrics_path: {{metricsPath}}{{/metricsPath}}
    static_configs:
      - targets: ['{{targetsIpPort}}']
        {{#hasLabels}}
        labels:
          {{#labels}}
          {{key}}: '{{value}}'
          {{/labels}}
        {{/hasLabels}}
  {{/jobs}}

alerting:
  alertmanagers:
    - static_configs:
      - targets: ['{{alertmanagers.targetsIpPort}}']

rule_files:
  {{#rules}}
  - ./rules/{{.}}.yml
  {{/rules}}
""";
    private final RedisService redisService;

    private static final ObjectMapper mapper = new ObjectMapper();

    public PrometheusDtoService(RedisService redisService) {
        this.redisService = redisService;

        System.out.println("create");
        createPrometheusTemplate();
        System.out.println("set");
        setPrometheusServer1Setting();
        System.out.println("read");
        readPrometheusSettingByServer();
    }

    private void setPrometheusServer1Setting() {

        PrometheusYmlDto.Global global = new PrometheusYmlDto.Global("15s", "15s");

        PrometheusYmlDto.Job windowsJob = new PrometheusYmlDto.Job("windows", null, "host.docker.internal:9182", true, List.of(new PrometheusYmlDto.Label("env", "prod"), new PrometheusYmlDto.Label("role", "window")));
        PrometheusYmlDto.Job redisJob = new PrometheusYmlDto.Job("redis", null, "host.docker.internal:9182", false, null);
        PrometheusYmlDto.Job queueStatus = new PrometheusYmlDto.Job("queue-status", "/actuator/prometheus", "host.docker.internal:8081", false, null);

        PrometheusYmlDto.Alertmanagers alertmanagers = new PrometheusYmlDto.Alertmanagers("host.docker.internal:9093");

        PrometheusYmlDto prom = new PrometheusYmlDto(global, List.of(windowsJob, redisJob, queueStatus), alertmanagers, List.of("mem_rules", "queue_size"));

        try {
            redisService.saveJson("server1-prom-yml", mapper.writeValueAsString(prom));
        } catch (JsonProcessingException e) {

        }
    }

    private void readPrometheusSettingByServer() {
        try {
            log.info("redisService.readJson(\"server1-prom-yml\") : {}", redisService.readJson("server1-prom-yml"));
            PrometheusYmlDto promYml = mapper.readValue(redisService.readJson("server1-prom-yml"), PrometheusYmlDto.class);
            log.info("promYml{}", promYml);

            String template = redisService.readJson("prometheus.yml:template");
            log.info("template{}" , template);

            MustacheFactory mf = new DefaultMustacheFactory();
            Mustache mustache = mf.compile(new StringReader(template), "prom_tmpl");
            StringWriter sw = new StringWriter();
            mustache.execute(sw, promYml).flush();
            String rendered = sw.toString();

            Path out = Path.of("./server1_prometheus.yml");
            Files.writeString(out, rendered, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("IOException : ", e);
        }
    }

    private void createPrometheusTemplate() {
        redisService.saveJson("prometheus.yml:template", PROM_TEMPLATE);
    }

}
