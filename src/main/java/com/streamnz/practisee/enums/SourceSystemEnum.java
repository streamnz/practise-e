package com.streamnz.practisee.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author cheng hao
 * @Date 06/10/2025 14:41
 */
@Getter
public enum SourceSystemEnum {
    SCADA("SCADA"),
    EMS("EMS"),
    DMS("DMS");

    private String name;

    SourceSystemEnum(String name) {
        this.name = name;
    }

    @JsonCreator
    public static SourceSystemEnum fromString(String name) {
        return SourceSystemEnum.valueOf(name.toUpperCase());
    }
}
