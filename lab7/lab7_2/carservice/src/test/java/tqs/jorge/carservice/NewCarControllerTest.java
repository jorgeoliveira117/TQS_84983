package tqs.jorge.carservice;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tqs.jorge.carservice.controllers.CarController;
import tqs.jorge.carservice.models.Car;
import tqs.jorge.carservice.services.CarManagerService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarController.class)
public class NewCarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarManagerService service;

    private Car car;

    @BeforeEach
    public void setUp() throws Exception {
        car = new Car("Porsche", "Taycan");
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    public void whenPostCar_thenCreateCar(){

        when(service.save(Mockito.any())).thenReturn(car);

        RestAssuredMockMvc.given()
                .header("Content-type", "application/json")
                .and()
                .body(car)
            .when()
                .post("/api/cars")
            .then()
                .statusCode(201)
                .body("maker", equalTo(car.getMaker()));

        verify(service, times(1)).save(Mockito.any());
    }

    @Test
    public void givenManyCars_whenGetCars_thenReturnJsonArray() throws Exception {
        Car rimac = new Car("Rimac", "Concept Two");
        Car mclaren = new Car("McLaren", "Senna");

        List<Car> allCars = Arrays.asList(car, rimac, mclaren);

        when(service.getAllCars()).thenReturn(allCars);

        RestAssuredMockMvc.when()
                .get("/api/cars")
            .then()
                .statusCode(200)
                .body("maker", hasItems(car.getMaker(), rimac.getMaker(), mclaren.getMaker()));


        verify(service, times(1)).getAllCars();
    }

    @Test
    public void whenGetCar_thenReturnJsonCar() throws Exception {
        when(service.getCarDetails(2L)).thenReturn(Optional.of(car));

        RestAssuredMockMvc.when()
                .get("/api/cars/2")
                .then()
                .statusCode(200)
                .body("maker", equalTo(car.getMaker()))
                .body("model", equalTo(car.getModel()));


        verify(service, times(1)).getCarDetails(2L);
    }
}
