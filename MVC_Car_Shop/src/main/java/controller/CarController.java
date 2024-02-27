package controller;

import exceptions.BusinessException;
import controller.DAO.CarDAO;
import model.DTO.CarDTO;
import model.DTO.CarDetailDTO;
import model.DTO.CarManufacturerDTO;
import model.entity.Car;

import java.util.ArrayList;
import java.util.List;

public class CarController {
    private final List<Car> cars;
    private final CarDAO carDAO;

    public CarController() throws BusinessException {
        this.cars = CarDAO.readCars();
        this.carDAO = new CarDAO();
    }

    // Add a new car
    public void addCar(Car car) throws BusinessException {
        cars.add(car);
        CarDAO.writeCars(cars);
    }

    // Update a car
    public void updateCar(Car updatedCar) throws BusinessException {
        for (int i = 0; i < cars.size(); i++) {
            if (cars.get(i).id == updatedCar.id) {
                cars.set(i, updatedCar);
                CarDAO.writeCars(cars);
                return;
            }
        }
    }

    // Delete a car
    public void deleteCar(int carId) throws BusinessException {
        cars.removeIf(car -> car.id == carId);
        CarDAO.writeCars(cars);
    }

    // Get car details by ID
    public CarDetailDTO getCarDetailsById(int carId) {
        for (Car car : cars) {
            if (car.id == carId) {
                return car.toCarDetailDTO();
            }
        }
        return null;
    }

    // Get cars from a given manufacturer
    public List<CarManufacturerDTO> getCarsByManufacturer(String manufacturer) {
        List<CarManufacturerDTO> result = new ArrayList<>();
        for (Car car : cars) {
            if (car.manufacturer.equalsIgnoreCase(manufacturer)) {
                result.add(car.toCarManufacturerDTO());
            }
        }
        return result;
    }

    // Get cars with price lower than a given value
    public List<CarDTO> getCarsWithPriceLowerThan(long price) {
        List<CarDTO> result = new ArrayList<>();
        for (Car car : cars) {
            if (car.price < price) {
                result.add(car.toCarDTO());
            }
        }
        return result;
    }

    // Get all cars
    public List<CarDTO> getAllCars() {
        List<CarDTO> result = new ArrayList<>();
        for (Car car : cars) {
            result.add(car.toCarDTO());
        }
        return result;
    }
}