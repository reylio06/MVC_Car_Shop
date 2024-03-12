package controller;

import exceptions.BusinessException;
import controller.DAO.CarDAO;
import model.DTO.CarDTO;
import model.DTO.CarDetailDTO;
import model.DTO.CarManufacturerDTO;
import model.entity.Car;

import java.util.*;


public class CarController {
    // List to store Car objects
    private final List<Car> cars;
    private final CarDAO carDAO;

    // Constructor to initialize the CarController and load cars from the CSV file
    public CarController(){
        this.carDAO = new CarDAO();
        // Load cars from the CSV file using CarDAO
        this.cars = new ArrayList<>();
    }

    // Method to load cars from the CSV file
    public void loadCarsFromCSV() throws BusinessException {
        // Reading cars from the CSV file using CarDAO
        this.cars.addAll(carDAO.readCars());
    }

    public void addCarLogic(Car car) throws BusinessException{
        cars.add(car);
        carDAO.writeCars(cars);
    }

    public void updateCarLogic(Car updatedCar, int oldID) throws BusinessException {
        for (int i = 0; i < cars.size(); i++) {
            // Find the car with the specified ID
            if (cars.get(i).getId() == oldID) {
                // Update the car with the new details
                cars.set(i, updatedCar);
                carDAO.writeCars(cars);
                return;
            }
        }
    }

    // Delete a car
    public void deleteCarByID(int carId) throws BusinessException{
        cars.removeIf(car -> car.getId() == carId);
        carDAO.writeCars(cars);
    }

    public CarDetailDTO getCarDetailsById(int carId) {
        for (Car car : cars) {
            // Find the car with the specified ID
            if (car.getId() == carId) {
                // Convert car entity to CarDetailDTO and return
                return car.toCarDetailDTO();
            }
        }
        // Return null if no car with the specified ID is found
        return null;
    }

    // Get cars from a given manufacturer
    public List<CarManufacturerDTO> getCarsByManufacturerLogic(String manufacturer) {
        List<CarManufacturerDTO> result = new ArrayList<>();

        for (Car car : cars) {
            // Check if manufacturer matches
            if (car.getManufacturer().equalsIgnoreCase(manufacturer)) {
                // Convert car entity to CarManufacturerDTO and add to result list
                result.add(car.toCarManufacturerDTO());
            }
        }
        return result;
    }

    // Get cars with price lower than a given value
    public List<CarDTO> getCarsWithPriceLowerThanLogic(long price) {
        List<CarDTO> result = new ArrayList<>();

        for (Car car : cars) {
            if (car.getPrice() < price) {
                // Convert car entity to CarDTO and add to result list
                result.add(car.toCarDTO());
            }
        }
        return result;
    }

    // Get all cars
    public List<CarDTO> getAllCarsLogic() {
        List<CarDTO> result = new ArrayList<>();

        for (Car car : cars) {
            // Convert each car entity to CarDTO and add to result list
            result.add(car.toCarDTO());
        }
        return result;
    }

    public void getAllCars(){
        List<CarDTO> allCars = getAllCarsLogic();

        // Print details of all cars
        if (allCars.isEmpty()) {
            System.out.println("No cars found.");
        } else {
            for (CarDTO car : allCars) {
                System.out.println(car.toString());
            }
        }
    }
}