package com.example.plant_service.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.Date;

@Embeddable
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoricalDate {
    @Enumerated(EnumType.STRING)
    private HistoricalDateType type;
    private Date timestamp;
}
