package pt.jorge.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import pt.jorge.backend.entities.helper.CountryStatisticsResponse;

import java.util.Collections;


@SpringBootApplication
public class BackEndApplication {

    private static final Logger log = LoggerFactory.getLogger(BackEndApplication.class);


    public static void main(String[] args){
        SpringApplication.run(BackEndApplication.class, args);
    }
}
