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
    // List to store Car objects
    private final List<Car> cars;
    // private final CarDAO carDAO;

    // Constructor to initialize the CarController and load cars from the CSV file
    public CarController() throws BusinessException {
        // Load cars from the CSV file using CarDAO
        this.cars = CarDAO.readCars();
        // this.carDAO = new CarDAO();
    }

    // Add a new car
    public void addCar(Car car) throws BusinessException {
        cars.add(car);
        CarDAO.writeCars(cars);
    }

    // Update a car
    public void updateCar(Car updatedCar, int oldID) throws BusinessException {
        for (int i = 0; i < cars.size(); i++) {
            // Find the car with the specified ID
            if (cars.get(i).id == oldID) {
                // Update the car with the new details
                cars.set(i, updatedCar);
                CarDAO.writeCars(cars);
                return;
            }
        }
    }

    // Delete a car
    public void deleteCar(int carId) throws BusinessException {
        // Remove the car with the specified ID
        cars.removeIf(car -> car.id == carId);
        CarDAO.writeCars(cars);
    }

    // Get car details by ID
    public CarDetailDTO getCarDetailsById(int carId) {
        for (Car car : cars) {
            // Find the car with the specified ID
            if (car.id == carId) {
                // Convert car entity to CarDetailDTO and return
                return car.toCarDetailDTO();
            }
        }
        // Return null if no car with the specified ID is found
        return null;
    }

    // Get cars from a given manufacturer
    public List<CarManufacturerDTO> getCarsByManufacturer(String manufacturer) {
        List<CarManufacturerDTO> result = new ArrayList<>();

        for (Car car : cars) {
            // Check if manufacturer matches
            if (car.manufacturer.equalsIgnoreCase(manufacturer)) {
                // Convert car entity to CarManufacturerDTO and add to result list
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
                // Convert car entity to CarDTO and add to result list
                result.add(car.toCarDTO());
            }
        }
        return result;
    }

    // Get all cars
    public List<CarDTO> getAllCars() {
        List<CarDTO> result = new ArrayList<>();

        for (Car car : cars) {
            // Convert each car entity to CarDTO and add to result list
            result.add(car.toCarDTO());
        }
        return result;
    }
}