package com.example.actuator.metric.gauge;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "QUEUE_INFO")
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueueInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no", nullable = false)
    private Long id;

    @Column(name = "type")
    private String type;

    @Column(name = "msg_type", length = 32)
    private String msgType;

    @Column(name = "name", length = 32)
    private String name;

    @Column(name = "service")
    private String service;

    @Column(name = "`desc`", length = 80)
    private String desc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type", referencedColumnName = "id", insertable = false, updatable = false)
    private Code code;
}

