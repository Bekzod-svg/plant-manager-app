package com.example.plant_service.repository;

import com.example.plant_service.model.HydrogenInstallation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HydrogenInstallationRepository extends JpaRepository<HydrogenInstallation, Long> {
    Optional<HydrogenInstallation> findByUserId(Long userId);
}
