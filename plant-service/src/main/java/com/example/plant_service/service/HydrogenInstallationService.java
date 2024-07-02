package com.example.plant_service.service;

import com.example.plant_service.config.UserServiceClient;
import com.example.plant_service.model.*;
import com.example.plant_service.repository.HydrogenInstallationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class HydrogenInstallationService {
    private final HydrogenInstallationRepository plantRepository;
    private final UserServiceClient userServiceClient;
    public HydrogenInstallation createInstallation(StatusType status, Location location, Long userId){
        Optional<HydrogenInstallation> plant = plantRepository.findByUserId(userId);
        if(plant.isPresent()){
            throw new IllegalArgumentException("User already has an installation");
        }
        StatusType finalStatus = status != null ? status : StatusType.ACTIVE;
        User owner = userServiceClient.getUserById(userId);
        if(owner == null){
            throw new EntityNotFoundException("User with id " + userId + " not found");
        }

        HydrogenInstallation installation = new HydrogenInstallation(finalStatus, location, userId);
        installation.setOwner(owner);
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

    public HydrogenInstallation assignUserToInstallation(Long installationId, Long userId){
        HydrogenInstallation installation = plantRepository.findById(installationId).orElseThrow(
                () -> new EntityNotFoundException("Installation not found")
        );
//        User newOwner = userRepository.findById(owner.getId()).orElseThrow(
//                () -> new EntityNotFoundException("Owner not found")
//        );
        User owner = userServiceClient.getUserById(userId);
        if(owner != null){
            installation.setUserId(userId);
            installation.setOwner(owner);

            HistoricalDate historicalDate = new HistoricalDate(HistoricalDateType.USER_CHANGE, new Date());
            installation.getHistoricalDates().add(historicalDate);
        }else {
            throw new EntityNotFoundException("User with id " + userId + " not found");
        }


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
        if(request.getUserId() != null){
//            User owner = userRepository.findById(request.getOwner().getId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
            User owner = userServiceClient.getUserById(request.getUserId());
            if(owner != null){
                installation.setUserId(request.getUserId());
                installation.setOwner(owner);
                HistoricalDate historicalDate = new HistoricalDate(HistoricalDateType.USER_CHANGE, new Date());
                installation.getHistoricalDates().add(historicalDate);
            }else {
                throw new EntityNotFoundException("User with id " + request.getUserId() + " not found");
            }

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
        List<HydrogenInstallation> installations = plantRepository.findAll();
        installations.forEach(inst -> {
            User owner = userServiceClient.getUserById(inst.getUserId());
            inst.setOwner(owner);
        });
        return installations;
    }

    public HydrogenInstallation getInstallation(Long id){
        HydrogenInstallation installation = plantRepository.findById(id).orElseThrow();
        User owner = userServiceClient.getUserById(installation.getUserId());
        installation.setOwner(owner);
        return installation;
    }

    public List<HydrogenInstallation> searchInstallations(String query){
        List<HydrogenInstallation> installations = plantRepository.findAll();
        return installations.stream().filter(
                plant ->{
                    User owner = userServiceClient.getUserById(plant.getUserId());
                    plant.setOwner(owner);
                    return owner.getName().toLowerCase().contains(query.toLowerCase()) ||
                            owner.getSurname().toLowerCase().contains(query.toLowerCase()) ||
                            plant.getStatus().toString().toLowerCase().contains(query.toLowerCase()) ||
                            plant.getLocation().getLatitude().toString().contains(query.toLowerCase()) ||
                            plant.getLocation().getLongitude().toString().contains(query.toLowerCase());
    }
        ).collect(Collectors.toList());
    }



//    public List<HydrogenInstallation> getAllInstallationsSorted(String sortField){
//        Sort sort = Sort.by(Sort.Direction.ASC, sortField);
//        List<HydrogenInstallation> installations = plantRepository.findAll(sort);
//        installations.forEach(plant -> {
//            User owner = userServiceClient.getUserById(plant.getUserId());
//            plant.setOwner(owner);
//        });
//        return installations;
//    }

    public List<HydrogenInstallation> getAllInstallationsSorted(String sortField){
        List<HydrogenInstallation> installations = plantRepository.findAll();
        installations.forEach(plant -> {
            User owner = userServiceClient.getUserById(plant.getUserId());
            plant.setOwner(owner);
        });
        return installations.stream()
                .sorted(getComparator(sortField))
                .collect(Collectors.toList());

    }
    private Comparator<HydrogenInstallation> getComparator(String sortField) {
        switch (sortField) {
            case "owner":
                return Comparator.comparing(inst -> inst.getOwner().getName(), Comparator.nullsLast(String::compareToIgnoreCase));
            case "owner.surname":
                return Comparator.comparing(inst -> inst.getOwner().getSurname(), Comparator.nullsLast(String::compareToIgnoreCase));
            case "status":
                return Comparator.comparing(inst -> inst.getStatus().toString(), Comparator.nullsLast(String::compareToIgnoreCase));
            case "location":
                return Comparator.comparing(inst -> inst.getLocation().getLatitude(), Comparator.nullsLast(Double::compareTo));
            case "location.longitude":
                return Comparator.comparing(inst -> inst.getLocation().getLongitude(), Comparator.nullsLast(Double::compareTo));
            default:
                return Comparator.comparing(HydrogenInstallation::getId); // Default sorting by ID
        }
    }

    public HydrogenInstallation getInstallationByUserId(Long userId){
        Optional<HydrogenInstallation> plant = plantRepository.findByUserId(userId);
        if(plant.isPresent()){
            return plant.get();
        }
        return null;
    }

}
