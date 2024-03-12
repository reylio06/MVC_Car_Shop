package view;

import exceptions.BusinessException;
import controller.CarController;
import model.DTO.CarDTO;
import model.DTO.CarDetailDTO;
import model.DTO.CarManufacturerDTO;
import model.entity.Car;
import model.entity.FuelType;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CarShopView {
    // Controller for managing car data
    private final CarController carController;
    private Scanner scanner;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    private String productionDateString;
    private Timestamp productionDate;
    private long price;
    private String manufacturer;
    private String model;
    private FuelType fuelType;
    private int horsepower;
    private String errorMessage = null;

    // Constructor to initialize CarShopView with a CarController
    public CarShopView(CarController carController) {
        this.carController = carController;
        this.scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        System.out.println("Car Shop Menu:");
        System.out.println("1. Add a new car");
        System.out.println("2. Update a car");
        System.out.println("3. Delete a car");
        System.out.println("4. Get car details by ID");
        System.out.println("5. Get cars from a given manufacturer");
        System.out.println("6. Get cars with price lower than a given value");
        System.out.println("7. Get all cars");
        System.out.println("8. Exit");
    }

    public void handleUserInput() throws BusinessException {
        boolean exitLoop = false;
        String errorMessage = null;
        String operationName = null;
        try {
            int choice;
            do {
                displayMenu();
                try {
                    System.out.println("Enter your choice: ");
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Choice must be a valid int value.");
                    continue;
                }

                try {
                    switch (choice) {
                        case 1:
                            addCar();
                            break;
                        case 2:
                            updateCar();
                            break;
                        case 3:
                            errorMessage = deleteCar();
                            break;
                        case 4:
                            getCarDetails();
                            break;
                        case 5:
                            getCarsByManufacturer();
                            break;
                        case 6:
                            getCarsWithPriceLowerThan();
                            break;
                        case 7:
                            carController.getAllCars();
                            break;
                        case 8:
                            System.out.println("Exiting...");
                            exitLoop = true;
                            break;
                        default:
                            System.out.println("Invalid choice. Please enter a number between 1 and 8.");
                    }
                } catch (BusinessException e) {
                    System.out.println(e.getMessage());
                }
            } while (!exitLoop);
        } catch (InputMismatchException e) {
            throw new BusinessException(errorMessage, operationName);
        }
    }

    // Add a new car
    public void addCar() throws BusinessException {
        int vehicleId;

        try {
            System.out.println("Enter ID of the vehicle: ");
            vehicleId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new BusinessException("Vehicle ID must be a valid int value.", "addCar");
        }

        // Check if car with specified ID already exists
        if (carController.getCarDetailsById(vehicleId) != null) {
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
            throw new BusinessException("Invalid date format. Please use the format DD.MM.YYYY.", "addCar");
        }

        System.out.println("Fuel Type (GASOLINE, DIESEL, HYBRID, ELECTRIC): ");
        String fuelTypeString = scanner.nextLine().toUpperCase();
        try {
            fuelType = FuelType.valueOf(fuelTypeString);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Invalid fuel type. Fuel type must be one of: GASOLINE, DIESEL, HYBRID, ELECTRIC.", "addCar");
        }

        try {
            System.out.println("Horsepower: ");
            horsepower = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new BusinessException("Horsepower must be a valid int value.", "addCar");
        }

        Car newCar = new Car(vehicleId, manufacturer, model, price, productionDate, fuelType, horsepower);
        carController.addCarLogic(newCar);

        // Singleton implementation
        // CarDAOSingleton.getInstance().writeCars(cars);

        System.out.println("New car added successfully.");
    }

    public void updateCar() throws BusinessException {
        int carIdToUpdate;

        try {
            System.out.println("Enter car ID to update: ");
            carIdToUpdate = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new BusinessException("Vehicle ID must be a valid int value.", "updateCarLogic");
        }

        CarDetailDTO carToUpdate = carController.getCarDetailsById(carIdToUpdate);

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

            try {
                switch (fieldChoice) {

                    case 1:
                        System.out.println("Enter new ID: ");
                        int newId;

                        try {
                            newId = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            // Input is not an integer
                            System.out.println("Invalid input. Car ID must be an integer.");
                            return;
                        }

                        if (carController.getCarDetailsById(newId) != null) {
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
                            System.out.println("Invalid date format. Please use the format DD.MM.YYYY.");
                            return;
                        }
                        break;
                    case 6:

                        System.out.println("Fuel Type (GASOLINE, DIESEL, HYBRID, ELECTRIC): ");
                        String fuelTypeString = scanner.nextLine().toUpperCase();
                        try {
                            fuelType = FuelType.valueOf(fuelTypeString);
                        } catch (IllegalArgumentException e) {
                            throw new BusinessException("Invalid fuel type. Fuel type must be one of: GASOLINE, DIESEL, HYBRID, ELECTRIC.", "addCar");
                        }
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
            } catch (BusinessException e){
                System.out.println(e.getMessage());
            }
            Car updatedCar = new Car(newCarId, manufacturer, model, price, productionDate, fuelType, horsepower);
            carController.updateCarLogic(updatedCar, carIdToUpdate);
            System.out.println("Car updated successfully.");

        } else {
            errorMessage = "Car with ID " + carIdToUpdate + " not found.";
            System.out.println(errorMessage);
        }
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
        CarDetailDTO carToDelete = carController.getCarDetailsById(carIdToDelete);

        if (carToDelete != null) {
            // Delete the car if found
            carController.deleteCarByID(carIdToDelete);
            System.out.println("Car with ID " + carIdToDelete + " deleted successfully.");
        } else {
            errorMessage = "Car with ID " + carIdToDelete + " not found.";
            System.out.println(errorMessage);
        }
        return errorMessage;
    }

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
        CarDetailDTO carDetailDTO = carController.getCarDetailsById(carId);

        if (carDetailDTO != null) {
            // Display car details if found
            System.out.println("Car details: " + carDetailDTO);
        } else {
            errorMessage = "Car with ID " + carId + " not found.";
            System.out.println(errorMessage);
        }
    }
    public void getCarsByManufacturer() {
        // Should return a list -> CarShopView - System.out
        System.out.println("Enter manufacturer: ");
        String manufacturer = scanner.nextLine();
        String errorMessage;

        // Retrieve cars by manufacturer
        List<CarManufacturerDTO> carsByManufacturer = carController.getCarsByManufacturerLogic(manufacturer);

        if (!carsByManufacturer.isEmpty()) {
            // Display cars if found
            for (CarManufacturerDTO car : carsByManufacturer) {
                System.out.println(car.toString());
            }
        } else {
            errorMessage = "No cars found for manufacturer " + manufacturer;
            System.out.println(errorMessage);
        }
    }

    public void getCarsWithPriceLowerThan() throws BusinessException {
        long maxPrice;
        String errorMessage;

        try {
            System.out.println("Enter maximum price: ");
            maxPrice = Long.parseLong(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new BusinessException("Price must be a valid long value.", "getCarsWithPriceLowerThan");
        }

        // Retrieve cars with price lower than the given value
        List<CarDTO> carsWithPriceLowerThan = carController.getCarsWithPriceLowerThanLogic(maxPrice);

        if (!carsWithPriceLowerThan.isEmpty()) {
            // Display cars if found
            for (CarDTO car : carsWithPriceLowerThan) {
                System.out.println(car.toString());
            }
        } else {
            errorMessage = "No cars found with price lower than " + maxPrice;
            System.out.println(errorMessage);
        }
    }
}