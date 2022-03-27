package tqs.jorge.carservice;


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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(CarController.class)
class CarControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CarManagerService service;


    @BeforeEach
    public void setUp() throws Exception {
    }

    @Test
    public void whenPostCar_thenCreateCar() throws Exception {
        Car car = new Car("Porsche", "Taycan");

        when(service.save(Mockito.any())).thenReturn(car);

        mvc.perform(
                post("/api/cars").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(car)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.maker", is(car.getMaker())));

        verify(service, times(1)).save(Mockito.any());
    }
    @Test
    public void givenManyCars_whenGetCars_thenReturnJsonArray() throws Exception {
        Car porsche = new Car("Porsche", "Taycan");
        Car rimac = new Car("Rimac", "Concept Two");
        Car mclaren = new Car("McLaren", "Senna");

        List<Car> allCars = Arrays.asList(porsche, rimac, mclaren);

        when(service.getAllCars()).thenReturn(allCars);

        mvc.perform(
                get("/api/cars").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].maker", is(porsche.getMaker())))
                .andExpect(jsonPath("$[1].maker", is(rimac.getMaker())))
                .andExpect(jsonPath("$[2].maker", is(mclaren.getMaker())));

        verify(service, times(1)).getAllCars();
    }


    @Test
    public void whenGetCar_thenReturnJsonCar() throws Exception {
        Car car = new Car("Porsche", "Taycan");

        when(service.getCarDetails(2L)).thenReturn(Optional.of(car));

        mvc.perform(
                get("/api/cars/2").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(car)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.maker", is(car.getMaker())));

        verify(service, times(1)).getCarDetails(2L);
    }
}