package com.company.resourceapi.services.api;

import com.company.resourceapi.exceptions.BusinessException;
import com.company.resourceapi.model.Project;

public interface ProjectService {

    Project getProject(Long id) throws BusinessException;

    Project addProject(Project project) throws BusinessException;

    Project updateProject(Long projectId, Project update)throws BusinessException;
}
