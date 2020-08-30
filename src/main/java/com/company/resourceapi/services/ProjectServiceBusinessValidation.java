package com.company.resourceapi.services;

import com.company.resourceapi.entities.ProjectEntity;
import com.company.resourceapi.enums.ErrorCode;
import com.company.resourceapi.exceptions.BusinessException;
import com.company.resourceapi.model.Project;
import com.company.resourceapi.repositories.ProjectRepository;
import com.company.resourceapi.repositories.SdlcSystemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@AllArgsConstructor
@Service
public class ProjectServiceBusinessValidation {

    protected SdlcSystemRepository sdlcSystemRepository;
    protected ProjectRepository projectRepository;

    protected ModelMapper modelMapper;
    protected ObjectMapper objectMapper;

    public void doAddProjectBusinessValidation(Project project) throws BusinessException {
        validateProjectMandatoryFields(project);
        validateSystemExistence(project.getSdlcSystem().getId());
        validateProjectAlreadyExistWithProvidedSystem(project);
    }

    public void doUpdateProjectBusinessValidation(Long projectId, Project project)
        throws BusinessException {

        validateProjectExistence(projectId);

        if (StringUtils.isEmpty(project.getExternalId())) {
            throw new BusinessException(ErrorCode.MISSING_PROJECT_EXTERNAL_ID);
        }
        validateSystemExistence(project.getSdlcSystem().getId());
        final Optional<ProjectEntity> projectWithSameExternalIdAndSameSystemMaybe = projectRepository
            .findBySdlcSystemIdAndExternalId(project.getSdlcSystem().getId(),
                project.getExternalId());

        if (projectWithSameExternalIdAndSameSystemMaybe.isPresent()) {
            final ProjectEntity projectEntity = projectWithSameExternalIdAndSameSystemMaybe.get();
            if (!projectEntity.getId().equals(project.getId())) {
                throw new BusinessException(
                    ErrorCode.NEW_PROJECT_EXTERNAL_ID_CONFLICT_WITH_OTHER_EXTERNAL_ID_IN_SAME_SYSTEM);
            }
        }
    }

    public void validateProjectExistence(Long projectId) throws BusinessException {
        boolean exists = projectRepository.existsById(projectId);
        if (!exists) {
            throw new BusinessException(ErrorCode.NOT_EXIST);
        }
    }

    private void validateSystemExistence(Long sdlcSystem) throws BusinessException {
        if (null == sdlcSystem) {
            throw new BusinessException(ErrorCode.MISSING_SDLC_SYSTEM_ID);
        }
        boolean exists = sdlcSystemRepository.existsById(sdlcSystem);
        if (!exists) {
            throw new BusinessException(ErrorCode.PARENT_SYSTEM_DOES_NOT_EXIST);
        }
    }

    private void validateProjectAlreadyExistWithProvidedSystem(Project project)
        throws BusinessException {
        boolean projectAlreadyExist = projectRepository
            .existsBySdlcSystemIdAndExternalId(project.getSdlcSystem().getId(), project.getExternalId());
        if (projectAlreadyExist) {
            throw new BusinessException(ErrorCode.PROJECT_ALREADY_EXIST);
        }
    }

    private void validateProjectExternalIdExistence(@NotNull Project project) throws BusinessException {
        if (StringUtils.isEmpty(project.getExternalId())) {
            throw new BusinessException(ErrorCode.MISSING_PROJECT_EXTERNAL_ID);
        }
    }

    private void validateSdlcSystemExistence(@NotNull Project project) throws BusinessException {
        if (project.getSdlcSystem() == null || project.getSdlcSystem().getId() == null) {
            throw new BusinessException(ErrorCode.MISSING_SDLC_SYSTEM_ID);
        }
    }

    private void validateProjectMandatoryFields(Project project) throws BusinessException {
        validateProjectExternalIdExistence(project);
        validateSdlcSystemExistence(project);
    }
}
