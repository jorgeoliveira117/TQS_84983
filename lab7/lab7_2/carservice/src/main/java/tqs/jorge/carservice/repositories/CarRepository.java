package tqs.jorge.carservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tqs.jorge.carservice.models.Car;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    public Car findByCarId(Long id);
    public List<Car> findAll();
}
