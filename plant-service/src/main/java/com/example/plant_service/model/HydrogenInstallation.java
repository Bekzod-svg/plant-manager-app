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

//    @OneToOne
//    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true)
//    private User owner;

    @Column(name = "userId")
    private Long userId;
    @Transient
    private User owner;
    @ElementCollection
    private List<HistoricalDate> historicalDates;

    public HydrogenInstallation(StatusType status, Location location, Long userId){
        this.status = status;
        this.location = location;
        this.userId = userId;
        this.historicalDates = new ArrayList<>();
    }

    public HydrogenInstallation(){

    }

}
