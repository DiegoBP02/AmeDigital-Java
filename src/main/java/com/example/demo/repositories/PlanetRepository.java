package com.example.demo.repositories;

import com.example.demo.entities.Planet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlanetRepository extends JpaRepository<Planet, UUID> {
    Optional<Planet> findByName(String name);

    void deleteByName(String name);
}
