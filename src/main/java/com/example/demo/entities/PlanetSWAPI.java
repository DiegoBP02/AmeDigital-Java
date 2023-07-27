package com.example.demo.entities;

import com.example.demo.entities.dto.Result;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class PlanetSWAPI {
    @JsonProperty("count")
    private int count;
    @JsonProperty("results")
    private List<Result> result;
}