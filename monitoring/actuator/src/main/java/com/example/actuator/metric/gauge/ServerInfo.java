package com.example.actuator.metric.gauge;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "SERVER_INFO")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServerInfo {
    // ServerInfo ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    private Long id;

    // 서버명 (ex: SMS1, MMS1)
    private String name;

    // 서버 Host (ex: 128.0.0.1)
    private String host;

    // 서버 Port (ex: 8080)
    private int port;

    private String msgType;
    
    // 기타
    @Column(name = "`desc`")
    private String desc;

}