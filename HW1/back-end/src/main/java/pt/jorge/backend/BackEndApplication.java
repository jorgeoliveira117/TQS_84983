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

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
        return args -> {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            headers.set("X-RapidAPI-Host", "covid-193.p.rapidapi.com");
            headers.set("X-RapidAPI-Key", "8b7adc7dd1mshc90568bcfe94194p19bf1ajsnb9339fef134f");

            HttpEntity request = new HttpEntity(headers);

            String url = "https://covid-193.p.rapidapi.com/statistics?country=portugal";
            ResponseEntity<CountryStatisticsResponse> stats = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    CountryStatisticsResponse.class
            );
            log.info(stats.getBody().toString());
        };
    }
}
