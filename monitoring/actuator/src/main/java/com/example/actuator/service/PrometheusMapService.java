package com.example.actuator.service;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.JedisPooled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

// @Service
public class PrometheusMapService {

    private static final String PROM_TEMPLATE =
"""
global:
  evaluation_interval: {{global.evaluation_interval}}
  scrape_interval: {{global.scrape_interval}}

scrape_configs:
  {{#jobs}}
  - job_name: '{{job_name}}'
    {{#metrics_path}}metrics_path: {{metrics_path}}{{/metrics_path}}
    static_configs:
      - targets: ['{{targets_ip_port}}']
        {{#has_labels}}
        labels:
          {{#labels}}
          {{key}}: '{{value}}'
          {{/labels}}
        {{/has_labels}}
  {{/jobs}}

alerting:
  alertmanagers:
    - static_configs:
      - targets: ['{{alertmanagers.targets_ip_port}}']

rule_files:
  {{#rules}}
  - ./rules/{{rule}}.yml
  {{/rules}}
""";
    private final RedisService redisService;

    private final JedisPooled jedis;
    @Value("${spring.data.redis.host}")
    private String redisHost;
    @Value("${spring.data.redis.port}")
    private int redisPort;
    @Value("${spring.data.redis.password}")
    private String redisPassword;

    public PrometheusMapService(RedisService redisService) {
        this.redisService = redisService;
        this.jedis = new JedisPooled(this.redisHost, this.redisPort, Boolean.parseBoolean(this.redisPassword));

        System.out.println("create");
        createPrometheusTemplate();
        System.out.println("set");
        setPrometheusServer1Setting();
        System.out.println("read");
        readPrometheusSettingByServer();
    }

    private Map<String, Object> setPrometheusServer1Setting() {

        Map<String, Object> root = new LinkedHashMap<>();

        Map<String, String> global = Map.of(
                "evaluation_interval", "15s",
                "scrape_interval", "15s"
        );
        root.put("global", global);

        List<Map<String, Object>> jobs = new ArrayList<>();
        jobs.add(job(
                "windows",
                null,
                "host.docker.internal:9182",
                List.of(label("env","prod"), label("role", "window"))
        ));
        jobs.add(job(
                "redis",
                null,
                "redis-exporter:9121",
                null
        ));
        jobs.add(job(
                "queue-status",
                "/actuator/prometheus",
                "host.docker.internal:8081",
                null
        ));
        root.put("jobs", jobs);
        Map<String , String> alertmanagers = Map.of(
                "targets_ip_port", "host.docker.internal:9093"
        );
        root.put("alertmanagers", alertmanagers);

        List<Map<String, Object>> rules = new ArrayList<>();
        rules.add(Map.of(
                "rule", "*"
        ));
        root.put("rules", rules);


        try {
            MustacheFactory mf = new DefaultMustacheFactory();
            Mustache mustache = mf.compile(new StringReader(PROM_TEMPLATE), "prom_tmpl");
            StringWriter sw = new StringWriter();
            mustache.execute(sw, root).flush();
            String rendered = sw.toString();
            System.out.println(rendered);

            Path out = Path.of("./server1_prometheus.yml");
            Files.writeString(out, rendered, StandardCharsets.UTF_8);
            System.out.println("rendered -> " + out.toAbsolutePath());
        } catch (IOException e) {
            return null;
        }
        return root;
    }

    private void readPrometheusSettingByServer() {
        System.out.println(redisService.readMap("server1"));
    }

    private void createPrometheusTemplate() {
        redisService.saveJson("prometheus.yml:template", PROM_TEMPLATE);
    }


    private static Map<String, String> label(String k, String v) {
        return new LinkedHashMap<>(Map.of("key", k, "value", v));
    }

    private static Map<String, Object> job(String name, String metricsPath, String targets, List<Map<String,String>> labels) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("job_name", name);
        if (metricsPath != null) m.put("metrics_path", metricsPath);
        m.put("targets_ip_port", targets);
        if (labels != null && !labels.isEmpty()) {
            m.put("labels", labels);
            m.put("has_labels", true);
        }
        return m;
    }

}
