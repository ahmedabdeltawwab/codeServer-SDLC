package com.company.resourceapi.repositories;

import com.company.resourceapi.entities.ProjectEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    Optional<ProjectEntity> findBySdlcSystemIdAndId(Long sdlcSystemId, Long projectId);

    Optional<ProjectEntity> findBySdlcSystemIdAndExternalId(Long sdlcSystemId, String externalId);

    boolean existsBySdlcSystemIdAndExternalId(Long sdlcSystemId, String externalId);
}
