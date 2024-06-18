package com.example.plant_service.controller;

import com.example.plant_service.dto.InstallationRequest;
import com.example.plant_service.model.HydrogenInstallation;
import com.example.plant_service.model.Location;
import com.example.plant_service.model.User;
import com.example.plant_service.repository.HydrogenInstallationRepository;
import com.example.plant_service.service.HydrogenInstallationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/installations")
@RequiredArgsConstructor
public class HydrogenInstallationController {

    private final HydrogenInstallationService plantService;

    @GetMapping
    public List<HydrogenInstallation> getAllInstallations(){
        return plantService.getAllInstallations();
    }

    @GetMapping("/{id}")
    public HydrogenInstallation getInstallation(@PathVariable Long id){
        return plantService.getInstallation(id);
    }

    @PostMapping
    public HydrogenInstallation createInstallation(@RequestBody InstallationRequest request){
        return plantService.createInstallation(request.getLocation(), request.getOwner());
    }


    @PutMapping("/{id}/user")
    public HydrogenInstallation assignUserToInstallation(@PathVariable Long id, @RequestBody User owner){
        return plantService.assignUserToInstallation(id, owner);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HydrogenInstallation> updateInstallation(@PathVariable Long id, @RequestBody HydrogenInstallation installation){
        return plantService.updateInstallation(id, installation);
    }

    @DeleteMapping("/{id}")
    public void deleteInstallation(@PathVariable Long id){
        plantService.deleteInstallation(id);
    }
}
