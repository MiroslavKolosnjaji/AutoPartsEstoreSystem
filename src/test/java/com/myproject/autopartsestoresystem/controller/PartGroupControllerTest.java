package com.myproject.autopartsestoresystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.autopartsestoresystem.dto.PartGroupDTO;
import com.myproject.autopartsestoresystem.exception.service.PartGroupNotFoundException;
import com.myproject.autopartsestoresystem.model.Part;
import com.myproject.autopartsestoresystem.model.PartGroup;
import com.myproject.autopartsestoresystem.model.PartGroupType;
import com.myproject.autopartsestoresystem.service.PartGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * @author Miroslav Kološnjaji
 */
@WebMvcTest(controllers = PartGroupController.class)
@MockBean(PartGroupService.class)
class PartGroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PartGroupService partGroupService;

    private ObjectMapper objectMapper;

    private PartGroupDTO partGroupDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        partGroupDTO = PartGroupDTO.builder().name(PartGroupType.BRAKING_SYSTEM).parts(new ArrayList<>()).build();
    }

    @DisplayName("Create Part Group")
    @Test
    void testCreatePartGroup_whenValidDetaislProvided_returnsCreatedPartGroupDTO() throws Exception {

        //given
        when(partGroupService.save(any(PartGroupDTO.class))).thenReturn(partGroupDTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(PartGroupController.PARTGROUP_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partGroupDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();
        PartGroupDTO savedDTO = objectMapper.readValue(response, PartGroupDTO.class);

        assertNotNull(savedDTO, "saved part group cannot be null");
        assertEquals(partGroupDTO.getName(), savedDTO.getName());

        verify(partGroupService).save(any(PartGroupDTO.class));
    }

    @DisplayName("Create Part Group When Invalid Details Provided - Returns Status 400")
    @Test
    void testCreatePartGroup_whenPartGroupNameIsNotProvided_returns400StatusCode() throws Exception {

        //given
        partGroupDTO.setName(null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(PartGroupController.PARTGROUP_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partGroupDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 400 expected");
    }

    @DisplayName("Update Part Group")
    @Test
    void testUpdatePartGroup_whenValidPartGroupDetailsProvided_returns204StatusCode() throws Exception {

        //given
        partGroupDTO.setId(1L);
        partGroupDTO.setName(PartGroupType.BODYWORK);

        when(partGroupService.update(any(Long.class), any(PartGroupDTO.class))).thenReturn(partGroupDTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(PartGroupController.PARTGROUP_URI_WITH_ID, partGroupDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partGroupDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 204 expected");
        verify(partGroupService).update(any(Long.class), any(PartGroupDTO.class));

    }

    @DisplayName("Update Part Group When Invalid Details Provided - Returns Status 400")
    @Test
    void testUpdatePartGroup_whenPartGroupNameIsNotProvided_returns400StatusCode() throws Exception {

        //given
        partGroupDTO.setId(1L);
        partGroupDTO.setName(null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(PartGroupController.PARTGROUP_URI_WITH_ID, partGroupDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partGroupDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 400 expected");
    }

    @DisplayName("Get All Part Groups")
    @Test
    void testGetAllPartGroups_whenListIsPopulated_returnsOnePartGroup() throws Exception {

        //given
        List<PartGroupDTO> partGroups = List.of(partGroupDTO);
        when(partGroupService.getAll()).thenReturn(partGroups);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(PartGroupController.PARTGROUP_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partGroups));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(1, objectMapper.readValue(result.getResponse().getContentAsString(), List.class).size(), "Expected 1 model to be returned");
        verify(partGroupService).getAll();
    }

    @DisplayName("Get Part Group By ID")
    @Test
    void testGetPartGroup_whenValidIdProvided_thenReturnsPartGroupDTOWithId1() throws Exception {

        //given
        partGroupDTO.setId(1L);
        when(partGroupService.getById(anyLong())).thenReturn(partGroupDTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(PartGroupController.PARTGROUP_URI_WITH_ID, partGroupDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partGroupDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String response =result.getResponse().getContentAsString();
        PartGroupDTO foundDTO = objectMapper.readValue(response, PartGroupDTO.class);

        assertNotNull(foundDTO, "Found part group cannot be null");
        assertEquals(partGroupDTO.getName(), foundDTO.getName());
    }

    @DisplayName("Get Part Group By ID When Invalid ID Provided - Returns 404 Status Code")
    @Test
    void testGetPartGroupById_whenInvalidIdProvided_returns404StatusCode() throws Exception {

        //given
        partGroupDTO.setId(99L);
        when(partGroupService.getById(partGroupDTO.getId())).thenThrow(PartGroupNotFoundException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(PartGroupController.PARTGROUP_URI_WITH_ID, partGroupDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 404 expected");
        verify(partGroupService).getById(partGroupDTO.getId());
    }

    @DisplayName("Delete Part Group")
    @Test
    void testDeletePartGroupById_whenValidIdProvided_returns204StatusCode() throws Exception {

        //given
        partGroupDTO.setId(99L);
        doNothing().when(partGroupService).delete(partGroupDTO.getId());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(PartGroupController.PARTGROUP_URI_WITH_ID, partGroupDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 204 expected");
        verify(partGroupService).delete(partGroupDTO.getId());
    }

    @DisplayName("Delete Part Group When Invalid ID Provided - Returns 404 Status Code")
    @Test
    void testDeletePartGroupById_whenInvalidIdProvided_returns404StatusCode() throws Exception {

        //given
        final String MESSAGE = "Part Group not found";
        partGroupDTO.setId(99L);
        doThrow(new PartGroupNotFoundException(MESSAGE)).when(partGroupService).delete(partGroupDTO.getId());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(PartGroupController.PARTGROUP_URI_WITH_ID, partGroupDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String response =result.getResponse().getContentAsString();


        //then
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 204 expected");
        assertTrue(response.contains(MESSAGE), "Expected " + MESSAGE + " but got " + response);
        verify(partGroupService).delete(partGroupDTO.getId());
    }
}