package com.example.actuator.metric.gauge;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "CODE_TYPE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeType {
    @Id
    @Column(name = "id", length = 5)
    private String id;

    @Column(name = "name", length = 45)
    private String name;

    @Column(name = "desc", length = 45)
    private String desc;
}