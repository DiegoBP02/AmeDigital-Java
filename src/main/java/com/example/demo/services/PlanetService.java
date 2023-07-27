package com.example.demo.services;

import com.example.demo.entities.Planet;
import com.example.demo.entities.PlanetSWAPI;
import com.example.demo.entities.dto.PlanetDTO;
import com.example.demo.entities.dto.Result;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repositories.PlanetRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PlanetService {

    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private APIService apiService;

    public Planet create(PlanetDTO planetDTO) {
        try {
            String planetName = planetDTO.getName();
            PlanetSWAPI planetSWAPI = apiService.findPlanetByName(planetName);

            int filmAppearances = 0;
            List<Result> results = planetSWAPI.getResult();
            if (!results.isEmpty()) {
                filmAppearances = results.get(0).getFilms().size();
            }

            Planet planet = Planet.builder()
                    .name(planetDTO.getName())
                    .climate(planetDTO.getClimate())
                    .land(planetDTO.getLand())
                    .filmAppearancesCount(filmAppearances)
                    .build();

            return planetRepository.save(planet);
        } catch (
                DataIntegrityViolationException e) {
            String errorMessage = e.getRootCause() != null ? e.getRootCause().getMessage() : e.getMessage();
            throw new DuplicateKeyException(errorMessage);
        }
    }

    public List<Planet> findAll() {
        return planetRepository.findAll();
    }

    public Planet findByName(String name) {
        return planetRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("name", name));
    }

    public Planet findById(UUID id) {
        return planetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Transactional
    public void deleteByName(String name) {
        findByName(name);
        planetRepository.deleteByName(name);
    }

    public void deleteById(UUID id) {
        findById(id);
        planetRepository.deleteById(id);
    }
}
