package spring.monitoring.entity;

import jakarta.persistence.Embeddable;
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
