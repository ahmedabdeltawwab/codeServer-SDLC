package com.company.resourceapi.services.impl;

import com.company.resourceapi.entities.ProjectEntity;
import com.company.resourceapi.enums.ErrorCode;
import com.company.resourceapi.exceptions.BusinessException;
import com.company.resourceapi.model.Project;
import com.company.resourceapi.repositories.ProjectRepository;
import com.company.resourceapi.repositories.SdlcSystemRepository;
import com.company.resourceapi.services.ProjectServiceBusinessValidation;
import com.company.resourceapi.services.api.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProjectServiceImpl extends ProjectServiceBusinessValidation implements ProjectService {

    public ProjectServiceImpl(SdlcSystemRepository sdlcSystemRepository,
        ProjectRepository projectRepository, ModelMapper modelMapper, ObjectMapper objectMapper) {
        super(sdlcSystemRepository, projectRepository, modelMapper, objectMapper);
    }

    @Override
    public Project getProject(Long id) throws BusinessException {
        log.debug("getProject: getting project {} ", id);
        return projectRepository.findById(id)
            .map(projectEntity -> modelMapper.map(projectEntity, Project.class))
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST));
    }

    @Override
    public Project addProject(Project project) throws BusinessException {
        log.debug("addProject: doing business validation for project {} ", project);
        doAddProjectBusinessValidation(project);
        log.debug("addProject: business validation passed for project {} ", project);

        log.debug("addProject: persisting project {} ", project);
        ProjectEntity projectEntity = modelMapper.map(project, ProjectEntity.class);
        projectEntity = projectRepository.save(projectEntity);
        log.debug("addProject: project created Id: {} ", project.getId());
        return modelMapper.map(projectEntity, Project.class);
    }

    @Override
    public Project updateProject(Long projectId, Project update)
        throws BusinessException {
        log.debug("updateProject: doing update validation for project {} ", projectId);
        doUpdateProjectBusinessValidation(projectId, update);
        log.debug("updateProject: updating project {} ", projectId);
        ProjectEntity projectEntity = modelMapper.map(update, ProjectEntity.class);
        projectRepository.save(projectEntity);
        log.debug("updateProject: project {} updated", projectId);
        return modelMapper.map(projectEntity, Project.class);
    }

}
