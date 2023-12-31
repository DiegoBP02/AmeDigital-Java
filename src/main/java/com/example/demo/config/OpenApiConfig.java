package com.example.demo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Diego",
                        email = "diegobpdev@gmail.com",
                        url = "https://portfolio-diegobp.netlify.app"
                ),
                description = "OpenApi documentation",
                title = "OpenApi specification",
                version = "1.0"
        ),
        servers = {
                @Server(
                        description = "Docker Local ENV",
                        url = "http://localhost:8080"
                )
        }
)

public class OpenApiConfig {
}
