package com.example.plant_service.model;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    private Double latitude;
    private Double longitude;
}
