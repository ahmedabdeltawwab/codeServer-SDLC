package com.company.resourceapi.facade.api;

import com.company.resourceapi.exceptions.BusinessException;
import com.company.resourceapi.model.Project;
import java.util.Map;

public interface ProjectFacade {

    Project getProject(Long id) throws BusinessException;

    Project addProject(Project project) throws BusinessException;

    Project updateProject(Long projectId, Map<String, Object> update)  throws BusinessException;
}
