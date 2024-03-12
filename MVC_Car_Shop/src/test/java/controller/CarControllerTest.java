package controller;

import exceptions.BusinessException;
import model.DTO.CarDTO;
import model.DTO.CarDetailDTO;
import model.entity.Car;
import model.entity.FuelType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Logger;


public class CarControllerTest {
    private CarController carController;
    private static final Logger LOGGER = Logger.getLogger(CarControllerTest.class.getName());

    @BeforeEach
    void setUp() {
        carController = new CarController();
        try {
            // Load cars from CSV before each test
            carController.loadCarsFromCSV();
        } catch (BusinessException e) {
            LOGGER.info("An error occurred while loading cars from CSV");
        }
    }

    @Test
    void testAddCar() {
        int initialSize = carController.getAllCarsLogic().size();

        // Simulate input data for the new car
        int vehicleId = 1001;
        String manufacturer = "TestManufacturer";
        String model = "TestModel";
        long price = 10000;
        String productionDateString = "01.01.2023"; // Example production date string
        Timestamp productionDate = null;
        FuelType fuelType = FuelType.GASOLINE;
        int horsepower = 200;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        try {
            productionDate = new Timestamp(dateFormat.parse(productionDateString).getTime());
        } catch (ParseException e) {
            LOGGER.info("Error parsing production date");
        }

        // Create a new Car object
        Car newCar = new Car(vehicleId, manufacturer, model, price, productionDate, fuelType, horsepower);

        try {
            // Add the new car using the testing method
            carController.addCarLogic(newCar);
        } catch (BusinessException e) {
            LOGGER.info("An error occurred while adding a car");
        }

        // Check if the size of the car list has increased by 1 after adding the new car
        int newSize = carController.getAllCarsLogic().size();
        Assertions.assertEquals(initialSize + 1, newSize);


        // Check if the added car details are correct
        CarDetailDTO addedCarDetails = carController.getCarDetailsById(vehicleId);
        Assertions.assertNotNull(addedCarDetails);
        Assertions.assertEquals(vehicleId, addedCarDetails.getId());
        Assertions.assertEquals(manufacturer, addedCarDetails.getManufacturer());
        Assertions.assertEquals(model, addedCarDetails.getModel());
        Assertions.assertEquals(price, addedCarDetails.getPrice());
        Assertions.assertEquals(productionDate, addedCarDetails.getProductionDate());
        Assertions.assertEquals(fuelType, addedCarDetails.getFuelType());
        Assertions.assertEquals(horsepower, addedCarDetails.getHorsepower());

        // Clean up: Delete the added car
        try {
            carController.deleteCarByID(vehicleId);
        } catch (BusinessException e) {
            LOGGER.info("An error occurred while deleting a car");
        }
    }

    @Test
    void testUpdateCarID() {
        int carIdToUpdate = 15;
        CarDetailDTO carDetails = carController.getCarDetailsById(carIdToUpdate);
        // Handle case where car with specified ID is not found
        if (carDetails == null) {
            System.out.println("Car with ID " + carIdToUpdate + " not found");
        }
        int originalId = carDetails.getId();

        // New ID for the car
        int newId = 999;

        Car updatedCar = new Car(newId, carDetails.getManufacturer(), carDetails.getModel(),
                carDetails.getPrice(), carDetails.getProductionDate(), carDetails.getFuelType(), carDetails.getHorsepower());

        try {
            carController.updateCarLogic(updatedCar, carIdToUpdate);
        } catch (BusinessException e) {
            LOGGER.info("An error occurred while updating car ID");
        }

        CarDetailDTO updatedCarDetails = carController.getCarDetailsById(newId);
        Assertions.assertNotNull(updatedCarDetails);
        Assertions.assertEquals(newId, updatedCarDetails.getId());

        // Revert changes
        Car revertCar = new Car(originalId, carDetails.getManufacturer(), carDetails.getModel(),
                carDetails.getPrice(), carDetails.getProductionDate(), carDetails.getFuelType(), carDetails.getHorsepower());

        try {
            carController.updateCarLogic(revertCar, newId);
        } catch (BusinessException e) {
            LOGGER.info("An error occurred while reverting changes");
        }
    }

    @Test
    void testUpdateCarManufacturer() {
        int carIdToUpdate = 15;
        CarDetailDTO carDetails = carController.getCarDetailsById(carIdToUpdate);
        String originalManufacturer = carDetails.getManufacturer();

        Car updatedCar = new Car(carIdToUpdate, "UpdatedManufacturer", carDetails.getModel(),
                carDetails.getPrice(), carDetails.getProductionDate(), carDetails.getFuelType(), carDetails.getHorsepower());

        try {
            carController.updateCarLogic(updatedCar, carIdToUpdate);
        } catch (BusinessException e) {
            LOGGER.info("An error occurred while updating car manufacturer");
        }

        CarDetailDTO updatedCarDetails = carController.getCarDetailsById(carIdToUpdate);
        Assertions.assertEquals("UpdatedManufacturer", updatedCarDetails.getManufacturer());

        // Revert changes
        Car revertCar = new Car(carIdToUpdate, originalManufacturer, carDetails.getModel(),
                carDetails.getPrice(), carDetails.getProductionDate(), carDetails.getFuelType(), carDetails.getHorsepower());

        try {
            carController.updateCarLogic(revertCar, carIdToUpdate);
        } catch (BusinessException e) {
            LOGGER.info("An error occurred while reverting changes");
        }
    }

    @Test
    void testUpdateCarModel() {
        int carIdToUpdate = 15;
        CarDetailDTO carDetails = carController.getCarDetailsById(carIdToUpdate);
        String originalModel = carDetails.getManufacturer();

        Car updatedCar = new Car(carIdToUpdate, carDetails.getManufacturer(), "Model",
                carDetails.getPrice(), carDetails.getProductionDate(), carDetails.getFuelType(), carDetails.getHorsepower());

        try {
            carController.updateCarLogic(updatedCar, carIdToUpdate);
        } catch (BusinessException e) {
            LOGGER.info("An error occurred while updating car model");
        }

        CarDetailDTO updatedCarDetails = carController.getCarDetailsById(carIdToUpdate);
        Assertions.assertEquals("Model", updatedCarDetails.getModel());

        // Revert changes
        Car revertCar = new Car(carIdToUpdate, carDetails.getManufacturer(), originalModel,
                carDetails.getPrice(), carDetails.getProductionDate(), carDetails.getFuelType(), carDetails.getHorsepower());

        try {
            carController.updateCarLogic(revertCar, carIdToUpdate);
        } catch (BusinessException e) {
            LOGGER.info("An error occurred while reverting changes");
        }
    }

    @Test
    void testUpdateCarPrice() {
        int carIdToUpdate = 15;
        CarDetailDTO carDetails = carController.getCarDetailsById(carIdToUpdate);
        long originalPrice = carDetails.getPrice();

        Car updatedCar = new Car(carIdToUpdate, carDetails.getManufacturer(), carDetails.getModel(),
                500000, carDetails.getProductionDate(), carDetails.getFuelType(), carDetails.getHorsepower());

        try {
            carController.updateCarLogic(updatedCar, carIdToUpdate);
        } catch (BusinessException e) {
            LOGGER.info("An error occurred while updating car price");
        }

        CarDetailDTO updatedCarDetails = carController.getCarDetailsById(carIdToUpdate);
        Assertions.assertEquals(500000, updatedCarDetails.getPrice());

        // Revert changes
        Car revertCar = new Car(carIdToUpdate, carDetails.getManufacturer(), carDetails.getModel(),
                originalPrice, carDetails.getProductionDate(), carDetails.getFuelType(), carDetails.getHorsepower());

        try {
            carController.updateCarLogic(revertCar, carIdToUpdate);
        } catch (BusinessException e) {
            LOGGER.info("An error occurred while reverting changes");
        }
    }


    @Test
    void testUpdateCarProductionDate() {
        int carIdToUpdate = 15;
        CarDetailDTO carDetails = carController.getCarDetailsById(carIdToUpdate);
        Timestamp originalProductionDate = carDetails.getProductionDate();

        String newProductionDateString = "01.01.2023";
        Timestamp newProductionDate = null;

        // Parse the new production date string to Timestamp
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        try {
            newProductionDate = new Timestamp(dateFormat.parse(newProductionDateString).getTime());
        } catch (ParseException e) {
            LOGGER.info("Error parsing production date");
        }

        Car updatedCar = new Car(carDetails.getId(), carDetails.getManufacturer(), carDetails.getModel(),
                carDetails.getPrice(), newProductionDate, carDetails.getFuelType(), carDetails.getHorsepower());

        try {
            carController.updateCarLogic(updatedCar, carIdToUpdate);
        } catch (BusinessException e) {
            LOGGER.info("An error occurred while updating car production date");
        }

        CarDetailDTO updatedCarDetails = carController.getCarDetailsById(carIdToUpdate);
        Assertions.assertNotNull(updatedCarDetails);
        Assertions.assertEquals(newProductionDate, updatedCarDetails.getProductionDate());

        // Revert changes
        Car revertCar = new Car(carDetails.getId(), carDetails.getManufacturer(), carDetails.getModel(),
                carDetails.getPrice(), originalProductionDate, carDetails.getFuelType(), carDetails.getHorsepower());

        try {
            carController.updateCarLogic(revertCar, carIdToUpdate);
        } catch (BusinessException e) {
            LOGGER.info("An error occurred while reverting changes");
        }
    }

    @Test
    void testUpdateCarFuelType() {
        int carIdToUpdate = 15;
        CarDetailDTO carDetails = carController.getCarDetailsById(carIdToUpdate);
        FuelType originalFuelType = carDetails.getFuelType();

        FuelType newFuelType = FuelType.DIESEL;

        Car updatedCar = new Car(carDetails.getId(), carDetails.getManufacturer(), carDetails.getModel(),
                carDetails.getPrice(), carDetails.getProductionDate(), newFuelType, carDetails.getHorsepower());

        try {
            carController.updateCarLogic(updatedCar, carIdToUpdate);
        } catch (BusinessException e) {
            LOGGER.info("An error occurred while updating car fuel type");
        }

        CarDetailDTO updatedCarDetails = carController.getCarDetailsById(carIdToUpdate);
        Assertions.assertNotNull(updatedCarDetails);
        Assertions.assertEquals(newFuelType, updatedCarDetails.getFuelType());

        // Revert changes
        Car revertCar = new Car(carDetails.getId(), carDetails.getManufacturer(), carDetails.getModel(),
                carDetails.getPrice(), carDetails.getProductionDate(), originalFuelType, carDetails.getHorsepower());

        try {
            carController.updateCarLogic(revertCar, carIdToUpdate);
        } catch (BusinessException e) {
            LOGGER.info("An error occurred while reverting changes");
        }
    }

    @Test
    void testUpdateCarHorsepower() {
        int carIdToUpdate = 15;
        CarDetailDTO carDetails = carController.getCarDetailsById(carIdToUpdate);
        int originalHorsepower = carDetails.getHorsepower();

        // New horsepower for the car
        int newHorsepower = 300;

        Car updatedCar = new Car(carDetails.getId(), carDetails.getManufacturer(), carDetails.getModel(),
                carDetails.getPrice(), carDetails.getProductionDate(), carDetails.getFuelType(), newHorsepower);

        try {
            carController.updateCarLogic(updatedCar, carIdToUpdate);
        } catch (BusinessException e) {
            LOGGER.info("An error occurred while updating car horsepower");
        }

        CarDetailDTO updatedCarDetails = carController.getCarDetailsById(carIdToUpdate);
        Assertions.assertNotNull(updatedCarDetails);
        Assertions.assertEquals(newHorsepower, updatedCarDetails.getHorsepower());

        // Revert changes
        Car revertCar = new Car(carDetails.getId(), carDetails.getManufacturer(), carDetails.getModel(),
                carDetails.getPrice(), carDetails.getProductionDate(), carDetails.getFuelType(), originalHorsepower);

        try {
            carController.updateCarLogic(revertCar, carIdToUpdate);
        } catch (BusinessException e) {
            LOGGER.info("An error occurred while reverting changes");
        }
    }

    @Test
    void testDeleteCar() {
        int vehicleIdToDelete = 15;

        // Get the car details of the vehicle to be deleted
        CarDetailDTO carDetailsToDelete = carController.getCarDetailsById(vehicleIdToDelete);

        // Store the details of the vehicle to be added back later
        String manufacturer = carDetailsToDelete.getManufacturer();
        String model = carDetailsToDelete.getModel();
        long price = carDetailsToDelete.getPrice();
        Timestamp productionDate = carDetailsToDelete.getProductionDate();
        int horsepower = carDetailsToDelete.getHorsepower();
        FuelType fuelType = carDetailsToDelete.getFuelType();

        // Delete the vehicle with the specified ID
        try {
            carController.deleteCarByID(vehicleIdToDelete);
        } catch (BusinessException e) {
            LOGGER.info("An error occurred while deleting car");
        }

        // Add a new vehicle with the same details back to the list
        Car newCar = new Car(vehicleIdToDelete, manufacturer, model, price, productionDate, fuelType, horsepower);
        try {
            carController.addCarLogic(newCar);
        } catch (BusinessException e) {
            LOGGER.info("An error occurred while adding car");
        }

        // Get the car details of the added vehicle
        CarDetailDTO addedCarDetails = carController.getCarDetailsById(vehicleIdToDelete);

        // Assert that the added vehicle details match the original details
        Assertions.assertNotNull(addedCarDetails);
        Assertions.assertEquals(manufacturer, addedCarDetails.getManufacturer());
        Assertions.assertEquals(model, addedCarDetails.getModel());
        Assertions.assertEquals(price, addedCarDetails.getPrice());
        Assertions.assertEquals(productionDate, addedCarDetails.getProductionDate());
        Assertions.assertEquals(horsepower, addedCarDetails.getHorsepower());
    }

    @Test
    void testGetCarDetailsForExistingCar() throws BusinessException {
        // Add a dummy car to the CarController
        Car dummyCar = new Car(1, "DummyManufacturer", "DummyModel",
                10000, new Timestamp(System.currentTimeMillis()), FuelType.GASOLINE, 150);
        carController.addCarLogic(dummyCar);

        // Call the getCarDetails method with the ID of the dummy car
        CarDetailDTO carDetailDTO = carController.getCarDetailsById(1);

        // Assertions to verify that the details returned by the method match the attributes of the dummy car
        Assertions.assertNotNull(carDetailDTO);
        Assertions.assertEquals(dummyCar.getId(), carDetailDTO.getId());
        Assertions.assertEquals(dummyCar.getManufacturer(), carDetailDTO.getManufacturer());
        Assertions.assertEquals(dummyCar.getModel(), carDetailDTO.getModel());
        Assertions.assertEquals(dummyCar.getPrice(), carDetailDTO.getPrice());
        Assertions.assertEquals(dummyCar.getProductionDate(), carDetailDTO.getProductionDate());
        Assertions.assertEquals(dummyCar.getFuelType(), carDetailDTO.getFuelType());
        Assertions.assertEquals(dummyCar.getHorsepower(), carDetailDTO.getHorsepower());

        // Delete the dummy car from the CarController
        carController.deleteCarByID(1);
    }

    @Test
    void testGetCarDetailsForNonExistingCar() {
        // Call the getCarDetails method with an ID of a non-existent car
        // Assuming car with ID 999 doesn't exist
        CarDetailDTO carDetailDTO = carController.getCarDetailsById(999);

        // Assertion to verify that the CarDetailDTO returned is null
        Assertions.assertNull(carDetailDTO);
    }

    @Test
    void testGetCarsWithPriceAbove() throws BusinessException {
        // Get the initial size of the car list
        int initialSize = carController.getAllCarsLogic().size();

        // Add two dummy cars with prices above the desired maximum price
        Car car1 = new Car(1, "DummyManufacturer1", "DummyModel1",
                11000, new Timestamp(System.currentTimeMillis()), FuelType.GASOLINE, 150);
        Car car2 = new Car(2, "DummyManufacturer2", "DummyModel2",
                12000, new Timestamp(System.currentTimeMillis()), FuelType.GASOLINE, 150);
        carController.addCarLogic(car1);
        carController.addCarLogic(car2);

        // Set the maximum price
        // Assuming the maximum price is 10000
        long maxPrice = 10000;

        // Call the getCarsWithPriceLowerThan method
        List<CarDTO> carsWithPriceLowerThan = carController.getCarsWithPriceLowerThanLogic(maxPrice);

        // Assertions to verify that the returned list is empty
        Assertions.assertTrue(carsWithPriceLowerThan.isEmpty());

        // Get the final size of the car list after adding the dummy cars
        int finalSize = carController.getAllCarsLogic().size();

        // Calculate the number of dummy cars added during the test
        int numDummyCarsAdded = finalSize - initialSize;

        // Delete the dummy cars from the end of the list based on the number of dummy cars added during the test
        for (int i = 0; i < numDummyCarsAdded; i++) {
            carController.deleteCarByID(finalSize - 1 - i);
        }
    }

    @Test
    void testGetCarsBelowMaxPrice() throws BusinessException {
        // Set the maximum price
        long maxPrice = 10000; // Assuming the maximum price is 10000

        // Add a dummy car with a price below the desired maximum price
        Car car = new Car(1, "DummyManufacturer", "DummyModel",
                9000, new Timestamp(System.currentTimeMillis()), FuelType.GASOLINE, 190);

        // Assert that the car's price is below the maximum price
        Assertions.assertTrue(car.getPrice() < maxPrice, "The price of the car is not below the maximum price.");

        // Add the car to the list
        carController.addCarLogic(car);

        // Call the getCarsWithPriceLowerThan method
        List<CarDTO> carsWithPriceLowerThan = carController.getCarsWithPriceLowerThanLogic(maxPrice);

        // Assertions to verify that the returned list contains the dummy car added
        Assertions.assertEquals(1, carsWithPriceLowerThan.size());

        carController.deleteCarByID(1);
    }

    @Test
    void testGetAllCarsLogic() throws BusinessException {
        int initialSize = carController.getAllCarsLogic().size();
        // Create some dummy cars and add them to the controller
        Car car1 = new Car(1, "DummyManufacturer1", "DummyModel1",
                9000, new Timestamp(System.currentTimeMillis()), FuelType.GASOLINE, 150);
        Car car2 = new Car(2, "DummyManufacturer2", "DummyModel2",
                8000, new Timestamp(System.currentTimeMillis()), FuelType.GASOLINE, 150);
        carController.addCarLogic(car1);
        carController.addCarLogic(car2);

        // Call the getAllCarsLogic method to get all cars
        List<CarDTO> allCars = carController.getAllCarsLogic();

        // Assert that the returned list is not null and contains the correct number of cars
        Assertions.assertNotNull(allCars);
        Assertions.assertEquals(initialSize + 2, allCars.size());

        // Clean up: Delete the dummy cars from the controller
        carController.deleteCarByID(1);
        carController.deleteCarByID(2);
    }
}