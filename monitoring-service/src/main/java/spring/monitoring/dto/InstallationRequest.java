package spring.monitoring.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import spring.monitoring.entity.Location;
import spring.monitoring.entity.StatusType;

//@Getter
//@Setter
//@Data
public class InstallationRequest {
    private StatusType status;
    private Location location;
    private Long userId;

    public InstallationRequest(StatusType status, Location location, Long userId) {
        this.status = status;
        this.location = location;
        this.userId = userId;
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
}

