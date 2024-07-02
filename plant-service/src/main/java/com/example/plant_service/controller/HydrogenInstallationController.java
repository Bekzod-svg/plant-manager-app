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
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/api/v1/installations")
@RequiredArgsConstructor
public class HydrogenInstallationController {

    private final HydrogenInstallationService plantService;

    @GetMapping
    public List<HydrogenInstallation> getAllInstallations(@RequestParam(name = "sort", required = false) String sortField){
        if(sortField != null){
            return plantService.getAllInstallationsSorted(sortField);
        }else {
            return plantService.getAllInstallations();
        }
    }

    @GetMapping("/{id}")
    public HydrogenInstallation getInstallation(@PathVariable Long id){
        return plantService.getInstallation(id);
    }

    @PostMapping
    public HydrogenInstallation createInstallation(@RequestBody InstallationRequest request){
        return plantService.createInstallation(request.getStatus(), request.getLocation(), request.getUserId());
    }


    @PutMapping("/{id}/user")
    public HydrogenInstallation assignUserToInstallation(@PathVariable Long id, @RequestBody Long userId){
        return plantService.assignUserToInstallation(id, userId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HydrogenInstallation> updateInstallation(@PathVariable Long id, @RequestBody HydrogenInstallation installation){
        return plantService.updateInstallation(id, installation);
    }

    @DeleteMapping("/{id}")
    public void deleteInstallation(@PathVariable Long id){
        plantService.deleteInstallation(id);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<HydrogenInstallation> getInstallationByUserId(@PathVariable Long userId){
        HydrogenInstallation installation = plantService.getInstallationByUserId(userId);
        if (installation == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(installation);
    }

    @GetMapping("/search")
    public List<HydrogenInstallation> searchInstallations(@RequestParam String query){
        return plantService.searchInstallations(query);
    }
}
