package tqs.jorge.carservice.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tqs.jorge.carservice.models.Car;
import tqs.jorge.carservice.services.CarManagerService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CarController {

    private CarManagerService carManagerService;

    public CarController(CarManagerService cms){ this.carManagerService = cms; }

    @PostMapping("/cars")
    public ResponseEntity<Car> createCar(@RequestBody Car car){
        HttpStatus status = HttpStatus.CREATED;
        Car saved = carManagerService.save(car);
        return new ResponseEntity<>(saved, status);
    }

    @GetMapping("/cars")
    public List<Car> getAllCars(){
        return carManagerService.getAllCars();
    }

    @GetMapping("/cars/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable(value = "id") Long id){
        Optional<Car> car = carManagerService.getCarDetails(id);
        HttpStatus status;
        if(car.isEmpty())
            status = HttpStatus.BAD_REQUEST;
        else
            status = HttpStatus.OK;
        Car c = car.isEmpty() ? null : car.get();
        return new ResponseEntity<>(c, status);
    }
}
