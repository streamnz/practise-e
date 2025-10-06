package com.streamnz.practisee.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.streamnz.practisee.enums.SourceSystemEnum;
import lombok.*;

import java.time.Instant;

/**
 * @Author cheng hao
 * @Date 06/10/2025 14:38
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OutageEvent {

    // idempotent event ID from  todo distributed snowflakeId
    private String eventId;

    // 可选属性，有合理的默认值
    private String eventDescription;

    // SCADA, EMS, DMS
    private SourceSystemEnum sourceSystem;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Instant eventTime;

    // 0-10 high number means severe storm
    // 可选属性，有合理的默认值
    private int stormLevel;

    // critical need immediate attention
    // 可选属性，有合理的默认值
    private boolean isCritical;

    // other fields omitted for brevity

    public OutageEvent(String eventId, SourceSystemEnum sourceSystem, Instant eventTime) {
        this.eventId = eventId;
        this.sourceSystem = sourceSystem;
        this.eventTime = eventTime;
    }
}
