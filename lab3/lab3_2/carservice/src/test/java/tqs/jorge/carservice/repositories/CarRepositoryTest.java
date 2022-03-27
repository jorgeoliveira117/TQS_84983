package tqs.jorge.carservice.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import tqs.jorge.carservice.models.Car;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
class CarRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CarRepository carRepository;

    @Test
    public void whenValidId_thenReturnCar(){
        Car porsche = new Car("Porsche", "Taycan");
        entityManager.persistAndFlush(porsche);

        Car foundCar = carRepository.findByCarId(porsche.getCarId());

        assertThat(foundCar).isNotNull();
        assertThat(foundCar.getMaker()).isEqualTo("Porsche");
    }

    @Test
    public void whenInvalidId_thenReturnNull(){
        Car foundCar = carRepository.findByCarId(-22L);

        assertThat(foundCar).isNull();
    }

    @Test
    public void whenFindAll_thenReturn2Cars(){
        Car rimac = new Car("Rimac", "Concept Two");
        Car mclaren = new Car("McLaren", "Senna");

        entityManager.persist(rimac);
        entityManager.persist(mclaren);
        entityManager.flush();

        List<Car> foundCars = carRepository.findAll();
        assertThat(foundCars).hasSize(2).extracting(Car::getMaker).contains("Rimac", "McLaren");
    }

}