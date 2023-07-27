package com.example.demo.repositories;

import com.example.demo.entities.Planet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PlanetRepositoryTest {

    @Autowired
    private PlanetRepository planetRepository;

    Planet PLANET_RECORD = Planet.builder()
            .name("name")
            .land("land")
            .climate("climate")
            .filmAppearancesCount(1)
            .build();

    @BeforeEach
    void setup(){
        planetRepository.save(PLANET_RECORD);
    }

    @AfterEach
    void tearDown(){
        planetRepository.deleteAll();
    }

    @Test
    void findByName_givenPlanet_shouldReturnOptionalPlanet(){
        Optional<Planet> result = planetRepository.findByName(PLANET_RECORD.getName());
        assertEquals(Optional.of(PLANET_RECORD),result);
    }

    @Test
    void findByName_givenNoPlanet_shouldReturnOptionalEmpty(){
        Optional<Planet> result = planetRepository.findByName("random");
        assertEquals(Optional.empty(),result);
    }

    @Test
    void deleteByName_givenPlanet_shouldRemovePlanet(){
        List<Planet> planets;
        planets = planetRepository.findAll();
        assertEquals(1, planets.size());
        planetRepository.deleteByName(PLANET_RECORD.getName());
        planets = planetRepository.findAll();
        assertEquals(0, planets.size());
    }
}