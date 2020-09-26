package com.company.resourceapi.repositories;

import com.company.resourceapi.entities.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    
    Optional<ProjectEntity> findBySdlcSystemIdAndExternalId(Long sdlcSystemId, String externalId);

    boolean existsBySdlcSystemIdAndExternalId(Long sdlcSystemId, String externalId);
}
