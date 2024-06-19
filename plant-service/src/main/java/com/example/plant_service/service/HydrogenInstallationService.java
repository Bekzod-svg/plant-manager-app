package com.example.plant_service.service;

import com.example.plant_service.dto.InstallationRequest;
import com.example.plant_service.model.*;
import com.example.plant_service.repository.HydrogenInstallationRepository;
import com.example.plant_service.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class HydrogenInstallationService {
    private final HydrogenInstallationRepository plantRepository;
    private final UserRepository userRepository;
    public HydrogenInstallation createInstallation(StatusType status, Location location, User user){
        StatusType finalStatus = status != null ? status : StatusType.ACTIVE;
        HydrogenInstallation installation = new HydrogenInstallation(finalStatus, location, user);
        HistoricalDate historicalDate = new HistoricalDate(HistoricalDateType.CREATED, new Date());

        installation.getHistoricalDates().add(historicalDate);
        return plantRepository.save(installation);
    }



//    public HydrogenInstallation createInstallation(InstallationRequest request){
//        HydrogenInstallation installation = new HydrogenInstallation();
//        installation.setLocation(request.getLocation());
//        installation.setOwner(request.getOwner());
//        installation.setStatus(StatusType.ACTIVE);
//        return plantRepository.save(installation);
//    }

    public HydrogenInstallation assignUserToInstallation(Long installationId, User owner){
        HydrogenInstallation installation = plantRepository.findById(installationId).orElseThrow(
                () -> new EntityNotFoundException("Installation not found")
        );
//        User newOwner = userRepository.findById(owner.getId()).orElseThrow(
//                () -> new EntityNotFoundException("Owner not found")
//        );
        installation.setOwner(owner);

        HistoricalDate historicalDate = new HistoricalDate(HistoricalDateType.USER_CHANGE, new Date());
        installation.getHistoricalDates().add(historicalDate);

        return plantRepository.save(installation);
    }

    public ResponseEntity<HydrogenInstallation> updateInstallation(Long id, HydrogenInstallation request){
        HydrogenInstallation installation = plantRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Installation not found")
        );
        if(request.getStatus() != null){
            installation.setStatus(request.getStatus());
            HistoricalDate historicalDate = new HistoricalDate(HistoricalDateType.STATUS_CHANGE, new Date());
            installation.getHistoricalDates().add(historicalDate);
        }
        if(request.getLocation() != null){
            installation.setLocation(request.getLocation());
            HistoricalDate historicalDate = new HistoricalDate(HistoricalDateType.LOCATION_CHANGE, new Date());
            installation.getHistoricalDates().add(historicalDate);

        }
        if(request.getOwner() != null){
//            User owner = userRepository.findById(request.getOwner().getId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
            installation.setOwner(request.getOwner());
            HistoricalDate historicalDate = new HistoricalDate(HistoricalDateType.USER_CHANGE, new Date());
            installation.getHistoricalDates().add(historicalDate);
        }
//        if(request.getHistoricalDates() != null){
//            installation.getHistoricalDates().clear();
//            installation.setHistoricalDates(request.getHistoricalDates());
//        }

        if(installation != null){
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

    public List<HydrogenInstallation> searchInstallations(String query){
        List<HydrogenInstallation> installations = plantRepository.findAll();
        return installations.stream().filter(
                plant ->
                        plant.getOwner().getName().toLowerCase().contains(query.toLowerCase()) ||
                        plant.getStatus().toString().toLowerCase().contains(query.toLowerCase()) ||
                                plant.getLocation().getLatitude().toString().contains(query.toLowerCase()) ||
                                plant.getLocation().getLongitude().toString().contains(query.toLowerCase())
        ).collect(Collectors.toList());
    }

    public List<HydrogenInstallation> getAllInstallationsSorted(String sortField){
        Sort sort = Sort.by(Sort.Direction.ASC, sortField);
        return plantRepository.findAll(sort);
    }



}
