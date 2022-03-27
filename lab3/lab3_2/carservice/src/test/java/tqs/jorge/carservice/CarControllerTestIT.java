package tqs.jorge.carservice;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import tqs.jorge.carservice.controllers.CarController;
import tqs.jorge.carservice.models.Car;
import tqs.jorge.carservice.repositories.CarRepository;
import tqs.jorge.carservice.services.CarManagerService;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc

@AutoConfigureTestDatabase
//@TestPropertySource( locations = "application-integrationtest.properties")
class CarControllerTestIT {

    @Autowired
    private MockMvc mvc;

    // will need to use the server port for the invocation url
    @LocalServerPort
    int randomServerPort;

    // a REST client that is test-friendly
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CarRepository repository;



    @AfterEach
    public void resetDb() {
        repository.deleteAll();
    }



    @Test
    public void whenPostCar_thenCreateCar() throws Exception {
        Car car = new Car("Porsche", "Taycan");

        mvc.perform(post("/api/cars").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(car)));

        List<Car> foundCars = repository.findAll();
        assertThat(foundCars).extracting(Car::getMaker).contains("Porsche");
    }

    @Test
    public void givenManyCars_whenGetCars_thenReturnJsonArray() throws Exception {
        Car porsche = new Car("Porsche", "Taycan");
        Car rimac = new Car("Rimac", "Concept Two");
        Car mclaren = new Car("McLaren", "Senna");

        repository.save(porsche);
        repository.save(rimac);
        repository.save(mclaren);

        mvc.perform(
                get("/api/cars").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].maker", is(porsche.getMaker())))
                .andExpect(jsonPath("$[1].maker", is(rimac.getMaker())))
                .andExpect(jsonPath("$[2].maker", is(mclaren.getMaker())));
    }

    @Test
    public void whenGetCar_thenReturnJsonCar() throws Exception {
        Car car = new Car("Porsche", "Taycan");
        repository.save(car);

        String url = "/api/cars/" + car.getCarId();
        mvc.perform(
                get(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.maker", is(car.getMaker())));

    }
}