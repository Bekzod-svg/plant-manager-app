package com.example.plant_service.service;

import com.example.plant_service.dto.InstallationRequest;
import com.example.plant_service.model.HydrogenInstallation;
import com.example.plant_service.model.Location;
import com.example.plant_service.model.StatusType;
import com.example.plant_service.model.User;
import com.example.plant_service.repository.HydrogenInstallationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class HydrogenInstallationService {
    private final HydrogenInstallationRepository plantRepository;
    public HydrogenInstallation createInstallation(Location location, User user){
        HydrogenInstallation installation = new HydrogenInstallation();
        installation.setLocation(location);
        installation.setOwner(user);
        installation.setStatus(StatusType.ACTIVE);
        return plantRepository.save(installation);
    }

//    public HydrogenInstallation createInstallation(InstallationRequest request){
//        HydrogenInstallation installation = new HydrogenInstallation();
//        installation.setLocation(request.getLocation());
//        installation.setOwner(request.getOwner());
//        installation.setStatus(StatusType.ACTIVE);
//        return plantRepository.save(installation);
//    }

    public HydrogenInstallation assignUserToInstallation(Long installationId, User user){
        HydrogenInstallation installation = plantRepository.findById(installationId).orElseThrow();
        installation.setOwner(user);
        return plantRepository.save(installation);
    }

    public ResponseEntity<HydrogenInstallation> updateInstallation(Long id, HydrogenInstallation installation){
        HydrogenInstallation old = plantRepository.findById(id).orElseThrow();
        if(old != null){
            return ResponseEntity.ok(plantRepository.save(installation));
        }else {
            return ResponseEntity.notFound().build();
        }

    }

    public void deleteInstallation(Long id){
        plantRepository.deleteById(id);
    }

    public List<HydrogenInstallation> getAllInstallations(){
        return plantRepository.findAll();
    }

    public HydrogenInstallation getInstallation(Long id){
        return plantRepository.findById(id).orElseThrow();
    }

}
