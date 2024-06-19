package com.example.plant_service.repository;

import com.example.plant_service.model.HydrogenInstallation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HydrogenInstallationRepository extends JpaRepository<HydrogenInstallation, Long> {
}
