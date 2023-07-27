package com.example.demo.services;

import com.example.demo.ApplicationConfigTest;
import com.example.demo.entities.Planet;
import com.example.demo.entities.PlanetSWAPI;
import com.example.demo.entities.dto.PlanetDTO;
import com.example.demo.entities.dto.Result;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repositories.PlanetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PlanetServiceTest extends ApplicationConfigTest {

    @Autowired
    private PlanetService planetService;

    @MockBean
    private PlanetRepository planetRepository;

    @MockBean
    private APIService apiService;

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

    Result RESULT_RECORD = new Result();

    PlanetSWAPI PLANET_SWAPI_RECORD = new PlanetSWAPI();

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(PLANET_RECORD, "id", UUID.randomUUID());
        RESULT_RECORD.setFilms(Collections.singletonList("film"));
        PLANET_SWAPI_RECORD.setCount(1);
        PLANET_SWAPI_RECORD.setResult(Collections.singletonList(RESULT_RECORD));
    }

    @Test
    void create_givenPlanet_shouldSavePlanet() {
        when(apiService.findPlanetByName(PLANET_DTO_RECORD.getName()))
                .thenReturn(PLANET_SWAPI_RECORD);
        when(planetRepository.save(any(Planet.class)))
                .thenReturn(PLANET_RECORD);

        Planet result = planetService.create(PLANET_DTO_RECORD);
        assertEquals(PLANET_RECORD, result);

        verify(apiService,times(1)).findPlanetByName(PLANET_DTO_RECORD.getName());
        verify(planetRepository,times(1)).save(any(Planet.class));
    }

    @Test
    void create_givenPlanetAlreadyExists_shouldThrowDuplicateKeyException() {
        when(apiService.findPlanetByName(PLANET_DTO_RECORD.getName()))
                .thenReturn(PLANET_SWAPI_RECORD);
        when(planetRepository.save(any(Planet.class)))
                .thenThrow(DataIntegrityViolationException.class);

        assertThrows(DuplicateKeyException.class, () -> {
            planetService.create(PLANET_DTO_RECORD);
        });

        verify(apiService,times(1)).findPlanetByName(PLANET_DTO_RECORD.getName());
        verify(planetRepository,times(1)).save(any(Planet.class));
    }

    @Test
    void findAll_givenPlanetList_shouldReturnPlanetList() {
        List<Planet> planetList = Collections.singletonList(PLANET_RECORD);
        when(planetRepository.findAll()).thenReturn(planetList);

        List<Planet> result = planetService.findAll();

        assertEquals(planetList, result);

        verify(planetRepository, times(1)).findAll();
    }

    @Test
    void findByName_givenPlanet_shouldReturnOptionalPlanet() {
        when(planetRepository.findByName(PLANET_RECORD.getName()))
                .thenReturn(Optional.of(PLANET_RECORD));

        Planet result = planetService.findByName(PLANET_RECORD.getName());

        assertEquals(PLANET_RECORD, result);

        verify(planetRepository, times(1)).findByName(PLANET_RECORD.getName());
    }

    @Test
    void findByName_givenNoPlanet_shouldThrowResourceNotFoundException() {
        when(planetRepository.findByName(PLANET_RECORD.getName()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            planetService.findByName(PLANET_RECORD.getName());
        });

        verify(planetRepository, times(1)).findByName(PLANET_RECORD.getName());
    }

    @Test
    void findById_givenPlanet_shouldReturnOptionalPlanet() {
        when(planetRepository.findById(PLANET_RECORD.getId()))
                .thenReturn(Optional.of(PLANET_RECORD));

        Planet result = planetService.findById(PLANET_RECORD.getId());

        assertEquals(PLANET_RECORD, result);

        verify(planetRepository, times(1)).findById(PLANET_RECORD.getId());
    }

    @Test
    void findById_givenNoPlanet_shouldThrowResourceNotFoundException() {
        when(planetRepository.findById(PLANET_RECORD.getId()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            planetService.findById(PLANET_RECORD.getId());
        });

        verify(planetRepository, times(1)).findById(PLANET_RECORD.getId());
    }

    @Test
    void deleteByName_givenPlanet_shouldRemovePlanet() {
        when(planetRepository.findByName(PLANET_RECORD.getName()))
                .thenReturn(Optional.of(PLANET_RECORD));

        planetService.deleteByName(PLANET_RECORD.getName());

        verify(planetRepository, times(1)).findByName(PLANET_RECORD.getName());
        verify(planetRepository, times(1)).deleteByName(PLANET_RECORD.getName());
    }

    @Test
    void deleteByName_givenNoPlanet_shouldThrowResourceNotFoundException() {
        when(planetRepository.findByName(PLANET_RECORD.getName()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            planetService.deleteByName(PLANET_RECORD.getName());
        });

        verify(planetRepository, times(1)).findByName(PLANET_RECORD.getName());
        verify(planetRepository, times(0)).deleteByName(PLANET_RECORD.getName());
    }

    @Test
    void deleteById_givenPlanet_shouldRemovePlanet() {
        when(planetRepository.findById(PLANET_RECORD.getId()))
                .thenReturn(Optional.of(PLANET_RECORD));

        planetService.deleteById(PLANET_RECORD.getId());

        verify(planetRepository, times(1)).findById(PLANET_RECORD.getId());
        verify(planetRepository, times(1)).deleteById(PLANET_RECORD.getId());
    }

    @Test
    void deleteById_givenNoPlanet_shouldThrowResourceNotFoundException() {
        when(planetRepository.findById(PLANET_RECORD.getId()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            planetService.deleteById(PLANET_RECORD.getId());
        });

        verify(planetRepository, times(1)).findById(PLANET_RECORD.getId());
        verify(planetRepository, times(0)).deleteById(PLANET_RECORD.getId());
    }
}