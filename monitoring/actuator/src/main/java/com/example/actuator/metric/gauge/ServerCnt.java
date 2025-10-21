package com.example.actuator.metric.gauge;

import lombok.Data;

@Data
public class ServerCnt {
    private Long serverNo;
    private Long cnt;

    public ServerCnt(Long serverNo, Long cnt) {
        this.serverNo = serverNo;
        this.cnt = cnt;
    }
}
