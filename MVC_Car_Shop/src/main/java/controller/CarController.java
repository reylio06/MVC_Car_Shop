package controller;

import controller.DAO.CarDAOSingleton;
import exceptions.BusinessException;
import controller.DAO.CarDAO;
import model.DTO.CarDTO;
import model.DTO.CarDetailDTO;
import model.DTO.CarManufacturerDTO;
import model.entity.Car;
import model.entity.FuelType;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;


public class CarController {
    // List to store Car objects
    private final List<Car> cars;
    private final CarDAO carDAO;
    private static final Logger LOGGER = Logger.getLogger(CarController.class.getName());
    private static final Scanner scanner = new Scanner(System.in);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    private String productionDateString;
    private Timestamp productionDate;
    private long price;
    private String manufacturer;
    private String model;
    private FuelType fuelType;
    private int horsepower;
    private String errorMessage = null;

    // Constructor to initialize the CarController and load cars from the CSV file
    public CarController() throws BusinessException {
        this.carDAO = new CarDAO();
        // Load cars from the CSV file using CarDAO
        // this.cars = carDAO.readCars();
        // Load cars from CSV file using CarDAOSingleton
        this.cars = CarDAOSingleton.getInstance().readCars();
    }

    // Add a new car
    public void addCar() throws BusinessException {
        // Add a new car
        int vehicleId;

        try {
            System.out.println("Enter ID of the vehicle: ");
            vehicleId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new BusinessException("Vehicle ID must be a valid int value.", "addCar");
        }

        // Check if car with specified ID already exists
        if (getCarDetailsById(vehicleId) != null) {
            throw new BusinessException("A car with ID " + vehicleId + " already exists. Please choose another index.", "addCar");
        }

        System.out.println("Enter car details:");
        System.out.println("Manufacturer: ");
        manufacturer = scanner.nextLine();
        System.out.println("Model: ");
        model = scanner.nextLine();

        try {
            System.out.println("Price: ");
            price = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new BusinessException("Price must be a valid long value.", "addCar");
        }

        System.out.println("Production Date (DD.MM.YYYY): ");
        //scanner.nextLine(); // Consume newline character
        productionDateString = scanner.nextLine();

        try {
            Date parsedDate = DATE_FORMAT.parse(productionDateString);
            Timestamp parsedTimestamp = new Timestamp(parsedDate.getTime());

            // Check if the parsed production date is in the future
            if (parsedTimestamp.after(new Timestamp(System.currentTimeMillis()))) {
                throw new BusinessException("Production date cannot be in the future. Please enter a valid date.", "addCar");
            }

            productionDate = parsedTimestamp;
        } catch (ParseException e) {
            LOGGER.info("Invalid date format. Please use the format DD.MM.YYYY.");
        }

        System.out.println("Fuel Type (GASOLINE, DIESEL, HYBRID, ELECTRIC): ");
        fuelType = FuelType.valueOf(scanner.nextLine().toUpperCase());

        try {
            System.out.println("Horsepower: ");
            horsepower = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new BusinessException("Horsepower must be a valid int value.", "addCar");
        }

        Car newCar = new Car(vehicleId, manufacturer, model, price, productionDate, fuelType, horsepower);
        cars.add(newCar);

        // Basic implementation
        // carDAO.writeCars(cars);

        // Singleton implementation
        CarDAOSingleton.getInstance().writeCars(cars);

        LOGGER.info("New car added successfully.");
    }

    // Update a car
    public void updateCar() throws BusinessException {
        int carIdToUpdate;

        try {
            System.out.println("Enter car ID to update: ");
            carIdToUpdate = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new BusinessException("Vehicle ID must be a valid int value.", "updateCarLogic");
        }

        CarDetailDTO carToUpdate = getCarDetailsById(carIdToUpdate);

        if (carToUpdate != null) {
            // Initialize with current values
            manufacturer = carToUpdate.getManufacturer();
            model = carToUpdate.getModel();
            price = carToUpdate.getPrice();
            productionDate = carToUpdate.getProductionDate();
            fuelType = carToUpdate.getFuelType();
            horsepower = carToUpdate.getHorsepower();
            int newCarId = carIdToUpdate;

            System.out.println("Enter field to update:");
            System.out.println("1. ID");
            System.out.println("2. Manufacturer");
            System.out.println("3. Model");
            System.out.println("4. Price");
            System.out.println("5. Production Date");
            System.out.println("6. Fuel Type");
            System.out.println("7. Horsepower");

            int fieldChoice;

            try {
                System.out.println("Enter your choice: ");
                fieldChoice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                throw new BusinessException("Choice must be a valid int value.", "updateCarLogic");
            }

            switch (fieldChoice) {

                case 1:
                    System.out.println("Enter new ID: ");
                    int newId;

                    try {
                        newId = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        // Input is not an integer
                        throw new BusinessException("Invalid input. Car ID must be an integer.", "updateCarLogic");
                    }

                    if (getCarDetailsById(newId) != null) {
                        throw new BusinessException("A car with ID " + newId + " already exists. Please choose another ID.", "updateCar");
                    }

                    // Update carIdToUpdate with the new ID
                    newCarId = newId;
                    break;
                case 2:

                    System.out.println("Enter new manufacturer: ");
                    manufacturer = scanner.nextLine();
                    break;
                case 3:

                    System.out.println("Enter new model: ");
                    model = scanner.nextLine();
                    break;
                case 4:

                    try {
                        System.out.println("Enter new price: ");
                        price = Long.parseLong(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        throw new BusinessException("Price must be a valid long value.", "updateCar");
                    }
                    break;
                case 5:

                    System.out.println("Enter new production date (DD.MM.YYYY): ");
                    productionDateString = scanner.nextLine().trim(); // Trim whitespace

                    try {
                        Date parsedDate = DATE_FORMAT.parse(productionDateString);
                        Timestamp parsedTimestamp = new Timestamp(parsedDate.getTime());

                        // Check if the parsed production date is in the future
                        if (parsedTimestamp.after(new Timestamp(System.currentTimeMillis()))) {
                            throw new BusinessException("Production date cannot be in the future. Please enter a valid date.", "updateCar");
                        }

                        productionDate = parsedTimestamp;
                    } catch (ParseException e) {
                        LOGGER.info("Invalid date format. Please use the format DD.MM.YYYY.");
                    }
                    break;
                case 6:

                    System.out.println("Enter new fuel type (GASOLINE, DIESEL, HYBRID, ELECTRIC): ");
                    fuelType = FuelType.valueOf(scanner.nextLine().toUpperCase());
                    break;
                case 7:

                    try {
                        System.out.println("Enter new horsepower: ");
                        horsepower = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        throw new BusinessException("Horsepower must be a valid int value.", "updateCar");
                    }
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 7.");
                    return;
            }

            Car updatedCar = new Car(newCarId, manufacturer, model, price, productionDate, fuelType, horsepower);
            updateCar(updatedCar, carIdToUpdate);
            System.out.println("Car updated successfully.");

        } else {
            errorMessage = "Car with ID " + carIdToUpdate + " not found.";
            LOGGER.info(errorMessage);
        }
    }

    public void updateCar(Car updatedCar, int oldID) throws BusinessException {
        for (int i = 0; i < cars.size(); i++) {
            // Find the car with the specified ID
            if (cars.get(i).getId() == oldID) {
                // Update the car with the new details
                cars.set(i, updatedCar);
                // Basic implementation
                // carDAO.writeCars(cars);
                // Singleton implementation
                CarDAOSingleton.getInstance().writeCars(cars);
                return;
            }
        }
    }

    // Delete a car
    private void deleteCarByID(int carId) throws BusinessException{
        cars.removeIf(car -> car.getId() == carId);
        // Basic implementation
        // carDAO.writeCars(cars);
        // Singleton implementation
        CarDAOSingleton.getInstance().writeCars(cars);
    }

    public String deleteCar() throws BusinessException {
        int carIdToDelete;
        // Remove the car with the specified ID
        try {
            System.out.println("Enter car ID to delete: ");
            carIdToDelete = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new BusinessException("Car ID must be a valid int value.", "deleteCar");
        }

        // Retrieve car details by ID
        CarDetailDTO carToDelete = getCarDetailsById(carIdToDelete);

        if (carToDelete != null) {
            // Delete the car if found
            deleteCarByID(carIdToDelete);
            LOGGER.info("Car with ID " + carIdToDelete + " deleted successfully.");
        } else {
            errorMessage = "Car with ID " + carIdToDelete + " not found.";
            LOGGER.info(errorMessage);
        }
        return errorMessage;
    }


    // Get car details by ID
    public void getCarDetails() throws BusinessException {
        int carId;
        System.out.println("Enter car ID:");

        try {
            carId = scanner.nextInt();
        } catch (InputMismatchException e) {
            // Input is not an integer
            throw new BusinessException("Invalid input. Car ID must be an integer.", "getCarDetails");
        }

        // Consume newline
        scanner.nextLine();

        // Retrieve car details by ID
        CarDetailDTO carDetailDTO = getCarDetailsById(carId);

        if (carDetailDTO != null) {
            // Display car details if found
            LOGGER.info("Car details: " + carDetailDTO);
        } else {
            errorMessage = "Car with ID " + carId + " not found.";
            LOGGER.info(errorMessage);
        }
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

    public void getCarsByManufacturer(){
        System.out.println("Enter manufacturer: ");
        manufacturer = scanner.nextLine();

        // Retrieve cars by manufacturer
        List<CarManufacturerDTO> carsByManufacturer = getCarsByManufacturerLogic(manufacturer);

        if (!carsByManufacturer.isEmpty()) {
            // Display cars if found
            for (CarManufacturerDTO car : carsByManufacturer) {
                System.out.println(car.toString());
            }
        } else {
            errorMessage = "No cars found for manufacturer " + manufacturer;
            LOGGER.info(errorMessage);
        }
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

    public void getCarsWithPriceLowerThan() throws BusinessException {
        long maxPrice;
        try {
            System.out.println("Enter maximum price: ");
            maxPrice = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new BusinessException("Price must be a valid long value.", "getCarsWithPriceLowerThan");
        }

        // Retrieve cars with price lower than the given value
        List<CarDTO> carsWithPriceLowerThan = getCarsWithPriceLowerThanLogic(maxPrice);

        if (!carsWithPriceLowerThan.isEmpty()) {
            // Display cars if found
            for (CarDTO car : carsWithPriceLowerThan) {
                System.out.println(car.toString());
            }
        } else {
            errorMessage = "No cars found with price lower than " + maxPrice;
            LOGGER.info(errorMessage);
        }
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
        for (CarDTO car : allCars) {
            System.out.println(car.toString());
        }
    }
}