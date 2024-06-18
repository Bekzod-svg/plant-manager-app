package com.example.plant_service.dto;

import com.example.plant_service.model.Location;
import com.example.plant_service.model.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class InstallationRequest {
    private Location location;
    private User owner;
}
