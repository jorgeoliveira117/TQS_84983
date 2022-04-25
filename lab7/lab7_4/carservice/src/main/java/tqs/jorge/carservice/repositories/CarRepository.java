package tqs.jorge.carservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tqs.jorge.carservice.models.Car;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    public Optional<Car> findById(Long id);
    public List<Car> findAll();

    public Car findByMaker(String maker);
}
