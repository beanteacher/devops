package com.example.actuator.metric.gauge;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "CODE")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Code {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "type_id")
    private String typeId;

    @Column(name = "value", length = 45)
    private String value;

    @Column(length = 1)
    private String useYn;

    @Column(length = 45)
    private String desc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", referencedColumnName = "id", insertable = false, updatable = false)
    private CodeType codeType;
}