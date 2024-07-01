package spring.monitoring.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum StatusType {
    ACTIVE,
    INACTIVE,
    PENDING;

    @JsonCreator
    public static StatusType fromValue(String value) {
        try {
            return StatusType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Default to ACTIVE or any appropriate default
            return ACTIVE;
        }
    }
}
