package org.example.cropmonitoringsystembackend;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableMethodSecurity
@EnableWebSecurity
public class CropMonitoringSystemBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CropMonitoringSystemBackendApplication.class, args);
    }
    @Bean
    public ModelMapper mapper() {return new ModelMapper();}
}
