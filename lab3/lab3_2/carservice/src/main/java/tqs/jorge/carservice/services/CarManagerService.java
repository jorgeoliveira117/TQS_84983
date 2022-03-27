package tqs.jorge.carservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs.jorge.carservice.models.Car;
import tqs.jorge.carservice.repositories.CarRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CarManagerService{

    @Autowired
    private CarRepository carRepository;

    public Car save(Car car){
        return carRepository.save(car);
    }

    public List<Car> getAllCars(){
        return carRepository.findAll();
    }

    public Optional<Car> getCarDetails(Long id){
        return carRepository.findById(id);
    }
}
