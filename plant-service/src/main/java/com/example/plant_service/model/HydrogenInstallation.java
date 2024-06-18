package com.example.plant_service.model;

import com.netflix.appinfo.UniqueIdentifier;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.EnableMBeanExport;

import java.util.List;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HydrogenInstallation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private StatusType status;
    @Embedded
    private Location location;
    @ManyToOne
    private User owner;
    @ElementCollection
    private List<HistoricalDate> historicalDates;

}
