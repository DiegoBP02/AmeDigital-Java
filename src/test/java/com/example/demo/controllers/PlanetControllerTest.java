package com.example.demo.controllers;

import com.example.demo.ApplicationConfigTest;
import com.example.demo.entities.Planet;
import com.example.demo.entities.dto.PlanetDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.services.PlanetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PlanetControllerTest extends ApplicationConfigTest {

    private static final String PATH = "/planets";

    @MockBean
    private PlanetService planetService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    PlanetDTO PLANET_DTO_RECORD = PlanetDTO.builder()
            .name("name")
            .land("land")
            .climate("climate")
            .build();

    Planet PLANET_RECORD = Planet.builder()
            .name(PLANET_DTO_RECORD.getName())
            .land(PLANET_DTO_RECORD.getLand())
            .climate(PLANET_DTO_RECORD.getClimate())
            .filmAppearancesCount(1)
            .build();

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(PLANET_RECORD, "id", UUID.randomUUID());
    }

    @Test
    void create_givenPlanet_shouldSavePlanet() throws Exception {
        when(planetService.create(PLANET_DTO_RECORD)).thenReturn(PLANET_RECORD);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(PLANET_DTO_RECORD));

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(PLANET_RECORD)));

        verify(planetService, times(1)).create(PLANET_DTO_RECORD);
    }

    @Test
    void create_givenPlanetAlreadyExists_shouldThrowDuplicateKeyException() throws Exception {
        when(planetService.create(PLANET_DTO_RECORD)).thenThrow(DuplicateKeyException.class);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(PLANET_DTO_RECORD));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof DuplicateKeyException));

        verify(planetService, times(1)).create(PLANET_DTO_RECORD);
    }

    @Test
    void findAll_givenPlanetList_shouldReturnPlanetList() throws Exception {
        List<Planet> planetList = Collections.singletonList(PLANET_RECORD);
        when(planetService.findAll()).thenReturn(planetList);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .get(PATH)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(planetList.size())))
                .andExpect(jsonPath("[0].id", is(PLANET_RECORD.getId().toString())));

        verify(planetService, times(1)).findAll();
    }

    @Test
    void findByName_givenPlanet_shouldReturnOptionalPlanet() throws Exception {
        when(planetService.findByName(PLANET_RECORD.getName()))
                .thenReturn(PLANET_RECORD);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .get(PATH + "/name/" + PLANET_RECORD.getName())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(PLANET_RECORD)));

        verify(planetService, times(1)).findByName(PLANET_RECORD.getName());
    }

    @Test
    void findByName_givenNoPlanet_shouldThrowResourceNotFoundException() throws Exception {
        when(planetService.findByName(PLANET_RECORD.getName()))
                .thenThrow(ResourceNotFoundException.class);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .get(PATH + "/name/" + PLANET_RECORD.getName())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof ResourceNotFoundException));

        verify(planetService, times(1)).findByName(PLANET_RECORD.getName());
    }

    @Test
    void findById_givenPlanet_shouldReturnOptionalPlanet() throws Exception {
        when(planetService.findById(PLANET_RECORD.getId()))
                .thenReturn(PLANET_RECORD);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .get(PATH + "/id/" + PLANET_RECORD.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(PLANET_RECORD)));

        verify(planetService, times(1)).findById(PLANET_RECORD.getId());
    }

    @Test
    void findById_givenNoPlanet_shouldThrowResourceNotFoundException() throws Exception {
        when(planetService.findById(PLANET_RECORD.getId()))
                .thenThrow(ResourceNotFoundException.class);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .get(PATH + "/id/" + PLANET_RECORD.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof ResourceNotFoundException));

        verify(planetService, times(1)).findById(PLANET_RECORD.getId());
    }

    @Test
    void deleteByName_givenPlanet_shouldRemovePlanet() throws Exception {
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .delete(PATH + "/name/" + PLANET_RECORD.getName())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andExpect(status().isNoContent());

        verify(planetService, times(1)).deleteByName(PLANET_RECORD.getName());
    }

    @Test
    void deleteByName_givenNoPlanet_shouldThrowResourceNotFoundException() throws Exception {
        doThrow(ResourceNotFoundException.class)
                .when(planetService).deleteByName(PLANET_RECORD.getName());

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .delete(PATH + "/name/" + PLANET_RECORD.getName())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof ResourceNotFoundException));

        verify(planetService, times(1)).deleteByName(PLANET_RECORD.getName());
    }

    @Test
    void deleteById_givenPlanet_shouldRemovePlanet() throws Exception {
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .delete(PATH + "/id/" + PLANET_RECORD.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andExpect(status().isNoContent());

        verify(planetService, times(1)).deleteById(PLANET_RECORD.getId());
    }

    @Test
    void deleteById_givenNoPlanet_shouldThrowResourceNotFoundException() throws Exception {
        doThrow(ResourceNotFoundException.class)
                .when(planetService).deleteById(PLANET_RECORD.getId());

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .delete(PATH + "/id/" + PLANET_RECORD.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof ResourceNotFoundException));

        verify(planetService, times(1)).deleteById(PLANET_RECORD.getId());
    }

}