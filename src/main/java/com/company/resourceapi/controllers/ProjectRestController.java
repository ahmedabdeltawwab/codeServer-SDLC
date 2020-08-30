package com.company.resourceapi.controllers;

import com.company.resourceapi.exceptions.BusinessException;
import com.company.resourceapi.facade.api.ProjectFacade;
import com.company.resourceapi.model.Project;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(ProjectRestController.ENDPOINT)
@Api(produces = MediaType.APPLICATION_JSON_VALUE, tags = "Project")
public class ProjectRestController {

    private static final String OP_GET_A_PROJECT = "Get a Project";
    private static final String OP_CREATE_A_PROJECT = "Create a Project";
    private static final String OP_UPDATE_A_PROJECT = "Update a Project";

    public static final String ENDPOINT = "/api/v2/projects";
    public static final String ENDPOINT_ID = "/{id}";
    public static final String ENDPOINT_PROJECT_ID = "/{projectId}";

    private static final String API_PARAM_ID = "ID";
    private static final String API_PARAM_PROJECT = "ProjectId";

    public static final String PATH_VARIABLE_ID = "id";
    public static final String PATH_VARIABLE_PROJECT = "projectId";
    private static final String HEADER_NAME_LOCATION = "Location";
    private static final String API_V_2_PROJECTS = "/api/v2/projects/";

    @Autowired
    private ProjectFacade projectFacade;

    @ApiOperation(OP_GET_A_PROJECT)
    @GetMapping(ENDPOINT_ID)
    public Project getProject(
        @ApiParam(name = API_PARAM_ID, required = true) @PathVariable(PATH_VARIABLE_ID) Long projectId)
        throws BusinessException {
        log.debug("getProject: get project request for project id: {} ", projectId);

        return projectFacade.getProject(projectId);
    }

    @ApiOperation(OP_CREATE_A_PROJECT)
    @PostMapping()
    public ResponseEntity<Project> createProject(
        @ApiParam(name = API_PARAM_PROJECT, required = true) @RequestBody Project project)
        throws BusinessException {
        log.debug("createProject: create project request for project {}", project);
        Project persistedProject = projectFacade.addProject(project);
        log.debug("createProject: project created Id: {}", persistedProject.getId());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HEADER_NAME_LOCATION, API_V_2_PROJECTS);

        return ResponseEntity.status(HttpStatus.CREATED).headers(httpHeaders).body(persistedProject);
    }

    @ApiOperation(OP_UPDATE_A_PROJECT)
    @PatchMapping(ENDPOINT_PROJECT_ID)
    public ResponseEntity<Project> updateProject(
        @ApiParam(name = API_PARAM_ID, required = true) @PathVariable(PATH_VARIABLE_PROJECT) Long projectId,
        @ApiParam(name = API_PARAM_PROJECT, required = true) @RequestBody Map<String, Object> update)
        throws BusinessException {
        log.debug("updateProject: update project request for project Id: {} data {} ", projectId, update);
        Project project = projectFacade.updateProject(projectId, update);
        log.debug("updateProject: project updated, project Id: {} date {} ", projectId, project);

        return ResponseEntity.ok(project);
    }
}
