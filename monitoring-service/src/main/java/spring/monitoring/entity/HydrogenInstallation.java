package spring.monitoring.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
//@Data
//@Getter
//@Setter
@AllArgsConstructor
@ToString
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
    private InstallationUser owner;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public InstallationUser getOwner() {
        return owner;
    }

    public void setOwner(InstallationUser owner) {
        this.owner = owner;
    }

    public List<HistoricalDate> getHistoricalDates() {
        return historicalDates;
    }

    public void setHistoricalDates(List<HistoricalDate> historicalDates) {
        this.historicalDates = historicalDates;
    }
}
