package com.company.resourceapi;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class ProjectRestControllerTest {

    public static final String ENDPOINT = "/api/v2/projects/";

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
    }

    @Test
    public void givenCompleteProjectDataWhenCreateProjectThenReturnCreated() throws Exception {
        mockMvc.perform(post(ENDPOINT)
            .content("{\n"
                + "  \"externalId\":\"SAMPLEPROJECT\",\n"
                + "  \"name\":\"Sample Project\",\n"
                + "  \"sdlcSystem\": {\"id\": 3}\n"
                + "}")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.sdlcSystem.id").value("3"));
    }

    @Test
    public void givenProjectDataWithoutExternalIdWhenCreateProjectThenReturnBadRequest() throws Exception {
        mockMvc.perform(post(ENDPOINT)
            .content("{\n"
                + "  \"name\": \"Sample Project\",\n"
                + "  \"sdlcSystem\": {\"id\": 3}\n"
                + "}")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(-4));
    }

    @Test
    public void givenProjectDataWithExistingSdlcSystemWhenCreateProjectThenReturnConflict() throws Exception {
        mockMvc.perform(post(ENDPOINT)
            .content("{\n"
                + "  \"externalId\": \"SAMPLEPROJECT\",\n"
                + "  \"name\": \"Sample Project\",\n"
                + "  \"sdlcSystem\": {\"id\": 1}\n"
                + "}")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.code").value(-3));
    }

    @Test
    public void givenProjectDataWithNoneExistSdlcSystemWhenCreateProjectThenReturnBadRequest() throws Exception {
        mockMvc.perform(post(ENDPOINT)
            .content("{\n"
                + "  \"name\": \"Sample Project\",\n"
                + "  \"sdlcSystem\": {\"id\": 10}\n"
                + "}")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(-4));
    }

    @Test
    public void givenProjectWithoutSdlcSystemWhenCreateProjectThenReturnBadRequest() throws Exception {
        mockMvc.perform(post(ENDPOINT)
            .content("{\n"
                + "  \"externalId\": \"SAMPLEPROJECT\"\n"
                + "}")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(-7));
    }

    @Test
    public void givenProjectNameWhenUpdateProjectThenReturnOk() throws Exception {
        mockMvc.perform(patch(ENDPOINT + "/8")
            .content("{\n"
                + "  \"name\": \"updated name\"\n"
                + "}")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("updated name"));
    }

    @Test
    public void givenSdlcIdWhenUpdateProjectThenReturnOk() throws Exception {
        mockMvc.perform(patch(ENDPOINT + "/8")
            .content("{\n"
                + "  \"sdlcSystem\": {\n"
                + "    \"id\": 2\n"
                + "  }\n"
                + "}")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.sdlcSystem.id").value(2));
    }

    @Test
    public void givenSdlcIdExistWithSameExternalIdWhenUpdateProjectThenReturnConflict() throws Exception {
        mockMvc.perform(patch(ENDPOINT + "/1")
            .content("{\n"
                + "  \"sdlcSystem\": {\n"
                + "    \"id\": 2\n"
                + "  }\n"
                + "}")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.code").value(-6));
    }

    @Test
    public void givenExternalIdExistWithSameSdlcIdWhenUpdateProjectThenReturnConflict() throws Exception {
        mockMvc.perform(patch(ENDPOINT + "/2")
            .content("{\n"
                + "  \"externalId\": \"SAMPLEPROJECT\"\n"
                + "}")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.code").value(-6));
    }

    @Test
    public void givenSdlcIdNotExistWhenUpdateProjectThenReturnNotFound() throws Exception {
        mockMvc.perform(patch(ENDPOINT + "/1")
            .content("{\n"
                + "  \"sdlcSystem\": {\n"
                + "    \"id\": 30\n"
                + "  }\n"
                + "}")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value(-2));
    }

    @Test
    public void givenWrongProjectIdWhenUpdateProjectThenReturnNotFound() throws Exception {
        mockMvc.perform(patch(ENDPOINT + "/10")
            .content("{\n"
                + "  \"sdlcSystem\": {\n"
                + "    \"id\": 3\n"
                + "  }\n"
                + "}")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value(-1));
    }

    @Test
    public void givenEmptyExternalSystemIdWhenUpdateProjectThenReturnBadRequest() throws Exception {
        mockMvc.perform(patch(ENDPOINT + "/1")
            .content("{\n"
                + "  \"externalId\": \"\"\n"
                + "}")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(-4));
    }

    @Test
    public void givenIllegalSdlcSystemIdWhenUpdateProjectThenReturnBadRequest() throws Exception {
        mockMvc.perform(patch(ENDPOINT + "/1")
            .content("{\n"
                + "  \"sdlcSystem\": {\n"
                + "    \"id\": \"xyz\"\n"
                + "  }\n"
                + "}")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(-5));
    }
}
