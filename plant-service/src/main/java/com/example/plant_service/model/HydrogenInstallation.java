package com.example.plant_service.model;

import com.netflix.appinfo.UniqueIdentifier;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.EnableMBeanExport;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
public class HydrogenInstallation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private StatusType status;
    @Embedded
    private Location location;
    @OneToOne
    private User owner;
    @ElementCollection
    private List<HistoricalDate> historicalDates;

    public HydrogenInstallation(StatusType status, Location location, User owner){
        this.status = status;
        this.location = location;
        this.owner = owner;
        this.historicalDates = new ArrayList<>();
    }

    public HydrogenInstallation(){

    }

}
