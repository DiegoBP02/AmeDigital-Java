package com.example.demo.services;

import com.example.demo.entities.PlanetSWAPI;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class APIService {

    public PlanetSWAPI findPlanetByName(String planet) {
        String url = getPath(planet);
        RestTemplate template = new RestTemplate();
        return template.getForObject(url, PlanetSWAPI.class);
    }

    private String getPath(String planet){
        return "https://swapi.dev/api/planets?search=" + planet;
    }
}
