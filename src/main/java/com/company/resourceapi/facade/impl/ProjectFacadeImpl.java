package com.company.resourceapi.facade.impl;

import com.company.resourceapi.enums.ErrorCode;
import com.company.resourceapi.exceptions.BusinessException;
import com.company.resourceapi.facade.api.ProjectFacade;
import com.company.resourceapi.model.Project;
import com.company.resourceapi.services.api.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProjectFacadeImpl implements ProjectFacade {

    private final ProjectService projectService;

    private final ObjectMapper objectMapper;

    @Override
    public Project getProject(Long id) throws BusinessException {
        return projectService.getProject(id);
    }

    @Override
    public Project addProject(Project project) throws BusinessException {
        return projectService.addProject(project);
    }

    @Override
    public Project updateProject(Long projectId, Map<String, Object> update)
        throws BusinessException {
        Project persistedProject = projectService.getProject(projectId);
        log.debug("project exist {}", persistedProject.getId());
        Map<String, Object> map = objectMapper.convertValue(persistedProject, Map.class);
        map.putAll(update);
        try {
            persistedProject = objectMapper.convertValue(map, Project.class);
        } catch (RuntimeException e) {
            log.error("Exception: {} ", e);
            throw new BusinessException(ErrorCode.ILLEGAL_VALUES);
        }
        log.debug("requested data for project {} looks fine", projectId);
        return projectService.updateProject(projectId, persistedProject);
    }

}
