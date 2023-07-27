package com.example.demo.services;

import com.example.demo.ApplicationConfigTest;
import com.example.demo.entities.PlanetSWAPI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

class APIServiceTest extends ApplicationConfigTest {

    @Autowired
    private APIService apiService;

    @Test
    void findPlanetByName_givenValidPlanetName_shouldReturnPlanetDataFromAPI() {
        PlanetSWAPI planetSWAPI = apiService.findPlanetByName("Tatooine");
        assertEquals(1, planetSWAPI.getCount());
        assertEquals(1, planetSWAPI.getResult().size());
        assertEquals(5, planetSWAPI.getResult().get(0).getFilms().size());
    }

    @Test
    void findPlanetByName_givenInvalidPlanetName_shouldReturnNoPlanetFound() {
        PlanetSWAPI planetSWAPI = apiService.findPlanetByName("random");
        assertEquals(0, planetSWAPI.getCount());
    }

}