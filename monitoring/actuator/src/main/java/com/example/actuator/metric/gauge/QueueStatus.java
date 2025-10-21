package com.example.actuator.metric.gauge;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "QUEUE_STATUS")
@IdClass(QueueStatus.QueueStatusId.class)
public class QueueStatus {
    @Id
    @Column(name = "queue_no")
    private Long queueNo;

    @Id
    @Column(name = "server_no")
    private Long serverNo;

    @Id
    @Column(name = "status_time")
    private LocalDateTime statusTime;

    @Column(name = "head")
    private Integer head;

    @Column(name = "tail")
    private Integer tail;

    @Column(name = "count")
    private Integer count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "queue_no", referencedColumnName = "no", insertable = false, updatable = false)
    private QueueInfo queueInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "server_no", referencedColumnName = "no", insertable = false, updatable = false)
    private ServerInfo serverInfo;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QueueStatusId implements Serializable {
        private Long queueNo;
        private Long serverNo;
        private LocalDateTime statusTime;
    }

}