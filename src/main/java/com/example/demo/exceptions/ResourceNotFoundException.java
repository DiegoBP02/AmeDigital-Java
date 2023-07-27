package com.example.demo.exceptions;

import java.util.UUID;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {
        super("Resource not found");
    }

    public ResourceNotFoundException(UUID id) {
        super("Resource not found. Id: " + id);
    }

    public ResourceNotFoundException(String resource, String name) {
        super("Resource not found. " + resource + ": " + name);
    }
}
