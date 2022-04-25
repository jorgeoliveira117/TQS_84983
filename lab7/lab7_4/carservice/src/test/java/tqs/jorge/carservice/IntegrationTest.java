package tqs.jorge.carservice;


import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import tqs.jorge.carservice.models.Car;
import tqs.jorge.carservice.repositories.CarRepository;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = CarserviceApplication.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IntegrationTest {

    @Container
    public static PostgreSQLContainer container = new PostgreSQLContainer()
            .withUsername("admin")
            .withPassword("admin")
            .withDatabaseName("cars");

    @Autowired
    private CarRepository repository;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    @Autowired
    private MockMvc mvc;

    // will need to use the server port for the invocation url
    @LocalServerPort
    int randomServerPort;

    // a REST client that is test-friendly
    @Autowired
    private TestRestTemplate restTemplate;


    private Car car;

    @BeforeEach
    public void setUp() throws Exception {
        car = new Car("Tesla", "Model X");
        RestAssuredMockMvc.mockMvc(mvc);
    }

    @Order(1)
    @Test
    public void whenPostCar_thenCreateCar(){
        repository.save(car);

        RestAssuredMockMvc.given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(car)
                .when()
                    .post("/api/cars")
                .then()
                    .statusCode(201)
                    .body("maker", equalTo(car.getMaker()));

    }

    @Order(2)
    @Test
    public void whenGetCars_thenReturnJsonArray() throws Exception {
        String maker1 = "Porsche";
        String maker2 = "Rimac";
        String maker3 = "Audi";
        String maker4 = car.getMaker();


        RestAssuredMockMvc.when()
                .get("/api/cars")
                .then()
                .statusCode(200)
                .body("maker", hasItems(maker1, maker2, maker3));


    }

    @Order(3)
    @Test
    public void whenGetCarById_thenReturnJsonCar() throws Exception {
        Long id = repository.findByMaker(car.getMaker()).getId();

        RestAssuredMockMvc.when()
                    .get("/api/cars/"+id)
                .then()
                    .statusCode(200)
                    .body("maker", equalTo(car.getMaker()))
                    .body("model", equalTo(car.getModel()));


    }

    @Order(4)
    @Test
    public void givenManyCars_whenGetCars_thenReturnJsonArray() throws Exception {
        Car bmw = new Car("BMW", "i3");
        repository.save(bmw);
        Car honda = new Car("Honda", "Civic");
        repository.save(honda);


        RestAssuredMockMvc.when()
                    .get("/api/cars")
                .then()
                    .statusCode(200)
                    .body("maker", hasItems(car.getMaker(), bmw.getMaker(), honda.getMaker()));


    }
}