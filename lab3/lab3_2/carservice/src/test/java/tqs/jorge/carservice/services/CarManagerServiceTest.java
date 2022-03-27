package tqs.jorge.carservice.services;

import com.sun.source.tree.ModuleTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;
import tqs.jorge.carservice.models.Car;
import tqs.jorge.carservice.repositories.CarRepository;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarManagerServiceTest {

    @Mock(lenient = true)
    private CarRepository carRepository;

    @InjectMocks
    private CarManagerService service;

    @BeforeEach
    void setUp() {
        Car porsche = new Car("Porsche", "Taycan");
        porsche.setCarId(20L);

        Car rimac = new Car("Rimac", "Concept Two");
        Car mclaren = new Car("McLaren", "Senna");
        List<Car> allCars = Arrays.asList(porsche, rimac, mclaren);

        when(carRepository.findAll()).thenReturn(allCars);
        when(carRepository.findById(porsche.getCarId())).thenReturn(Optional.of(porsche));
        when(carRepository.findById(-1L)).thenReturn(Optional.empty());

    }

    @Test
    public void whenSave_thenSaveCar(){
        Car ferrari = new Car("Ferrari", "599XXE");
        when(carRepository.save(ferrari)).thenReturn(ferrari);

        Car savedCar = service.save(ferrari);

        verify(carRepository, VerificationModeFactory.times(1)).save(ferrari);
        assertThat(savedCar).isEqualTo(ferrari);
    }

    @Test
    public void whenGetAll_thenReturnAllCars(){
        List<Car> allCars = service.getAllCars();
        assertThat(allCars).hasSize(3).extracting(Car::getMaker).contains("Porsche", "Rimac", "McLaren");
    }

    @Test
    public void whenValidId_thenCarFound(){
        Optional<Car> car = service.getCarDetails(20L);
        assertThat(car.isPresent()).isTrue();
        assertThat(car.get().getMaker()).isEqualTo("Porsche");
    }

    @Test
    public void whenInvalidId_thenCarNotFound(){
        Optional<Car> car = service.getCarDetails(-1L);
        assertThat(car.isEmpty()).isTrue();
    }

}