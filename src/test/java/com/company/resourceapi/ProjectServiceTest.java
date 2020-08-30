package com.company.resourceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

import com.company.resourceapi.enums.ErrorCode;
import com.company.resourceapi.exceptions.BusinessException;
import com.company.resourceapi.model.Project;
import com.company.resourceapi.model.SdlcSystem;
import com.company.resourceapi.services.api.ProjectService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class ProjectServiceTest {

    private static final String TEST_PROJECT_NAME = "test project";
    private static final String TEST_EXTERNAL_ID = "test external";
    private static final String SAMPLEPROJECT_NAME = "SAMPLEPROJECT";
    private static final String NEW_PROJECT_NAME = "NEW NAME";
    private static final String NEW_EXTERNAL_ID = "NEW EXTERNAL ID";
    private static final long SDLC_SYSTEM_ID_1 = 1L;
    private static final long PROJECT_ID_1 = 1L;
    private static final long PROJECT_ID_10 = 10L;
    private static final long PROJECT_ID_3 = 3L;
    private static final long SDLC_SYSTEM_ID_3 = 3L;

    @Autowired
    private ProjectService projectService;

    @Mock
    private Project project;

    @Mock
    private SdlcSystem sdlcSystem;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void setUp() {
        when(project.getName()).thenReturn(TEST_PROJECT_NAME);
        when(project.getExternalId()).thenReturn(TEST_EXTERNAL_ID);
        when(project.getSdlcSystem()).thenReturn(sdlcSystem);
    }

    @Test
    public void givenProjectWhenCreateProjectThenProjectCreated() throws Exception {
        // arrange
        when(sdlcSystem.getId()).thenReturn(SDLC_SYSTEM_ID_1);
        // act
        Project actual = projectService.addProject(project);
        // assert
        assertFalse(null == actual.getId());
    }

    @Test
    public void givenProjectWithoutExternalIdWhenCreateProjectThenThrowBusinessException() throws Exception {
        // arrange
        when(project.getExternalId()).thenReturn(null);
        when(sdlcSystem.getId()).thenReturn(SDLC_SYSTEM_ID_1);
        // act
        exceptionRule.expect(BusinessException.class);
        exceptionRule.expect(new BusinessExceptionCustomMatcher(ErrorCode.MISSING_PROJECT_EXTERNAL_ID));
        projectService.addProject(project);
    }

    @Test
    public void givenProjectWithoutSdlcSystemWhenCreateProjectThenThrowBusinessException() throws Exception {
        // arrange
        when(project.getSdlcSystem()).thenReturn(sdlcSystem);
        when(sdlcSystem.getId()).thenReturn(null);
        // act
        exceptionRule.expect(BusinessException.class);
        exceptionRule.expect(new BusinessExceptionCustomMatcher(ErrorCode.MISSING_SDLC_SYSTEM_ID));
        projectService.addProject(project);
    }

    @Test
    public void givenProjectWithSdlcSystemAndExternalIdWhenCreateProjectThenThrowBusinessException() throws Exception {
        // arrange
        when(project.getExternalId()).thenReturn(SAMPLEPROJECT_NAME);
        when(sdlcSystem.getId()).thenReturn(SDLC_SYSTEM_ID_1);
        when(project.getSdlcSystem()).thenReturn(sdlcSystem);
        // act
        exceptionRule.expect(BusinessException.class);
        exceptionRule.expect(new BusinessExceptionCustomMatcher(ErrorCode.PROJECT_ALREADY_EXIST));
        projectService.addProject(project);
    }

    @Test
    public void givenNoneExistSdlcSystemWhenCreateProjectThenThrowBusinessException() throws Exception {
        // arrange
        when(sdlcSystem.getId()).thenReturn(PROJECT_ID_10);
        when(project.getSdlcSystem()).thenReturn(sdlcSystem);
        // act
        exceptionRule.expect(BusinessException.class);
        exceptionRule.expect(new BusinessExceptionCustomMatcher(ErrorCode.PARENT_SYSTEM_DOES_NOT_EXIST));
        projectService.addProject(project);
    }

    @Test
    public void givenNewProjectDataWhenUpdateProjectThenProjectUpdated() throws Exception {
        // arrange
        when(project.getName()).thenReturn(NEW_PROJECT_NAME);
        when(project.getExternalId()).thenReturn(NEW_EXTERNAL_ID);
        when(sdlcSystem.getId()).thenReturn(SDLC_SYSTEM_ID_3);
        // act
        Project actual = projectService.updateProject(PROJECT_ID_1, project);
        // assert
        assertEquals(project.getName(), actual.getName());
        assertEquals(project.getExternalId(), actual.getExternalId());
        assertEquals(sdlcSystem.getId(), actual.getSdlcSystem().getId());
    }

    @Test
    public void givenProjectWithSdlcSystemAndExternalIdWhenUpdateProjectThenThrowBusinessException() throws Exception {
        // arrange
        when(project.getExternalId()).thenReturn(SAMPLEPROJECT_NAME);
        when(sdlcSystem.getId()).thenReturn(SDLC_SYSTEM_ID_1);
        when(project.getSdlcSystem()).thenReturn(sdlcSystem);
        when(project.getId()).thenReturn(PROJECT_ID_3);
        // act
        exceptionRule.expect(BusinessException.class);
        exceptionRule.expect(new BusinessExceptionCustomMatcher(
            ErrorCode.NEW_PROJECT_EXTERNAL_ID_CONFLICT_WITH_OTHER_EXTERNAL_ID_IN_SAME_SYSTEM));
        projectService.updateProject(PROJECT_ID_3, project);
    }

    @Test
    public void givenNoneExistSdlcSystemWhenUpdateProjectThenThrowBusinessException() throws Exception {
        // arrange
        when(sdlcSystem.getId()).thenReturn(PROJECT_ID_10);
        when(project.getSdlcSystem()).thenReturn(sdlcSystem);
        // act
        exceptionRule.expect(BusinessException.class);
        exceptionRule.expect(new BusinessExceptionCustomMatcher(ErrorCode.PARENT_SYSTEM_DOES_NOT_EXIST));
        projectService.updateProject(PROJECT_ID_1, project);
    }

    @Test
    public void givenEmptyExternalIdWhenUpdateProjectThenThrowBusinessException() throws Exception {
        // arrange
        when(project.getExternalId()).thenReturn(null);
        when(project.getId()).thenReturn(PROJECT_ID_1);
        // act
        exceptionRule.expect(BusinessException.class);
        exceptionRule.expect(new BusinessExceptionCustomMatcher(ErrorCode.MISSING_PROJECT_EXTERNAL_ID));
        projectService.updateProject(PROJECT_ID_1, project);
    }

    @Test
    public void givenProjectWithoutSdlcSystemWhenUpdateProjectThenThrowBusinessException() throws Exception {
        // arrange
        when(project.getSdlcSystem()).thenReturn(sdlcSystem);
        when(sdlcSystem.getId()).thenReturn(null);
        when(project.getId()).thenReturn(PROJECT_ID_1);
        // act
        exceptionRule.expect(BusinessException.class);
        exceptionRule.expect(new BusinessExceptionCustomMatcher(ErrorCode.MISSING_SDLC_SYSTEM_ID));
        projectService.updateProject(PROJECT_ID_1, project);
    }
}
