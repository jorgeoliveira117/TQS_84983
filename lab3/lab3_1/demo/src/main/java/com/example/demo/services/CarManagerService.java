package com.example.demo.services;

import com.example.demo.models.Car;
import com.example.demo.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarManagerService{

    @Autowired
    private CarRepository carRepository;

    public Car save(Car car){
        return null;
    }

    public List<Car> getAllCars(){
        return null;
    }

    public Optional<Car> getCarDetails(Long id){
        return null;
    }
}
