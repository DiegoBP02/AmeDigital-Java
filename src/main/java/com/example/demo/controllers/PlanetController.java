package com.example.demo.controllers;

import com.example.demo.entities.Planet;
import com.example.demo.entities.PlanetSWAPI;
import com.example.demo.entities.dto.PlanetDTO;
import com.example.demo.services.APIService;
import com.example.demo.services.PlanetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/planets")
public class PlanetController {

    @Autowired
    private PlanetService planetService;

    @PostMapping
    public ResponseEntity<Planet> create(@Valid @RequestBody PlanetDTO planetDto) {
        Planet planet = planetService.create(planetDto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(planet.getId()).toUri();

        return ResponseEntity.created(uri).body(planet);
    }

    @GetMapping
    public ResponseEntity<List<Planet>> findAll() {
        return ResponseEntity.ok().body(planetService.findAll());
    }

    @GetMapping(value = "/name/{name}")
    public ResponseEntity<Planet> findByName(@PathVariable String name) {
        return ResponseEntity.ok().body(planetService.findByName(name));
    }

    @GetMapping(value = "/id/{id}")
    public ResponseEntity<Planet> findById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(planetService.findById(id));
    }

    @DeleteMapping(value = "/name/{name}")
    public ResponseEntity<Planet> deleteByName(@PathVariable String name) {
        planetService.deleteByName(name);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/id/{id}")
    public ResponseEntity<Planet> deleteById(@PathVariable UUID id) {
        planetService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
