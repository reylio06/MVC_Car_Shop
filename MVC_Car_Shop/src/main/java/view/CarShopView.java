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
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class CarShopView {
    private final CarController carController;
    private final Scanner scanner;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

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
        try {
            int choice;
            do {
                String manufacturer;
                String model;
                long price;
                String productionDateString;
                Timestamp productionDate;
                FuelType fuelType;
                int horsepower;
                displayMenu();
                System.out.println("Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                switch (choice) {
                    case 1:
                        // Add a new car
                        System.out.println("Enter car details:");
                        System.out.print("Manufacturer: ");
                        manufacturer = scanner.nextLine();
                        System.out.print("Model: ");
                        model = scanner.nextLine();
                        System.out.print("Price: ");
                        price = scanner.nextLong();
                        System.out.print("Production Date (DD.MM.YYYY): "); // Modify prompt
                        scanner.nextLine(); // Consume newline character
                        productionDateString = scanner.nextLine();
                        try {
                            Date parsedDate = DATE_FORMAT.parse(productionDateString);
                            productionDate = new Timestamp(parsedDate.getTime());
                        } catch (ParseException e) {
                            System.out.println("Invalid date format. Please use the format DD.MM.YYYY.");
                            continue; // Continue to the next iteration of the loop
                        }
                        System.out.print("Fuel Type (GASOLINE, DIESEL, HYBRID, ELECTRIC): ");
                        fuelType = FuelType.valueOf(scanner.nextLine().toUpperCase());
                        System.out.print("Horsepower: ");
                        horsepower = scanner.nextInt();
                        Car newCar = new Car(0, manufacturer, model, price, productionDate, fuelType, horsepower);
                        carController.addCar(newCar);
                        System.out.println("New car added successfully.");
                        break;
                    case 2:
                        // Update a car
                        System.out.println("Enter car ID to update:");
                        int carIdToUpdate = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        CarDetailDTO carToUpdate = carController.getCarDetailsById(carIdToUpdate);
                        if (carToUpdate != null) {
                            manufacturer = carToUpdate.getManufacturer();
                            model = carToUpdate.getModel(); // Initialize with current values
                            price = carToUpdate.getPrice(); // Initialize with current values
                            productionDate = carToUpdate.getProductionDate(); // Initialize with current values
                            fuelType = carToUpdate.getFuelType(); // Initialize with current values
                            horsepower = carToUpdate.getHorsepower(); // Initialize with current values

                            System.out.println("Enter field to update:");
                            System.out.println("1. Manufacturer");
                            System.out.println("2. Model");
                            System.out.println("3. Price");
                            System.out.println("4. Production Date");
                            System.out.println("5. Fuel Type");
                            System.out.println("6. Horsepower");
                            System.out.print("Enter your choice: ");
                            int fieldChoice = scanner.nextInt();
                            scanner.nextLine(); // Consume newline character
                            switch (fieldChoice) {
                                case 1:
                                    System.out.print("Enter new manufacturer: ");
                                    manufacturer = scanner.nextLine();
                                    break;
                                case 2:
                                    System.out.print("Enter new model: ");
                                    model = scanner.nextLine();
                                    break;
                                case 3:
                                    System.out.print("Enter new price: ");
                                    price = scanner.nextLong();
                                    break;
                                case 4:
                                    System.out.print("Enter new production date (DD.MM.YYYY): ");
                                    productionDateString = scanner.nextLine().trim(); // Trim whitespace
                                    try {
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                                        Date parsedDate = dateFormat.parse(productionDateString);
                                        productionDate = new Timestamp(parsedDate.getTime());
                                    } catch (ParseException e) {
                                        System.out.println("Invalid date format. Please use the format DD.MM.YYYY.");
                                        break; // Exit the switch case
                                    }
                                    break;
                                case 5:
                                    System.out.print("Enter new fuel type (GASOLINE, DIESEL, ELECTRIC): ");
                                    fuelType = FuelType.valueOf(scanner.nextLine().toUpperCase());
                                    break;
                                case 6:
                                    System.out.print("Enter new horsepower: ");
                                    horsepower = scanner.nextInt();
                                    break;
                                default:
                                    System.out.println("Invalid choice. Please enter a number between 1 and 6.");
                                    break;
                            }
                            Car updatedCar = new Car(carIdToUpdate, manufacturer, model, price, productionDate, fuelType, horsepower);
                            carController.updateCar(updatedCar);
                            System.out.println("Car updated successfully.");
                        } else {
                            System.out.println("Car with ID " + carIdToUpdate + " not found.");
                        }
                        break;
                    case 3:
                        // Delete a car
                        System.out.println("Enter car ID to delete:");
                        int carIdToDelete = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        CarDetailDTO carToDelete = carController.getCarDetailsById(carIdToDelete);
                        if (carToDelete != null) {
                            carController.deleteCar(carIdToDelete);
                            System.out.println("Car with ID " + carIdToDelete + " deleted successfully.");
                        } else {
                            System.out.println("Car with ID " + carIdToDelete + " not found.");
                        }
                        break;
                    case 4:
                        // Get car details by ID
                        System.out.println("Enter car ID:");
                        int carId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        CarDetailDTO carDetailDTO = carController.getCarDetailsById(carId);
                        if (carDetailDTO != null) {
                            System.out.println("Car details: " + carDetailDTO);
                        } else {
                            System.out.println("Car with ID " + carId + " not found.");
                        }
                        break;
                    case 5:
                        // Get cars from a given manufacturer
                        System.out.print("Enter manufacturer: ");
                        manufacturer = scanner.nextLine(); // Read manufacturer input
                        scanner.nextLine(); // Consume newline
                        List<CarManufacturerDTO> carsByManufacturer = carController.getCarsByManufacturer(manufacturer);
                        if (!carsByManufacturer.isEmpty()) {
                            for (CarManufacturerDTO car : carsByManufacturer) {
                                System.out.println(car.toString());
                            }
                        } else {
                            System.out.println("No cars found for manufacturer " + manufacturer);
                        }
                        break;
                    case 6:
                        // Get cars with price lower than a given value
                        System.out.println("Enter maximum price:");
                        long maxPrice = scanner.nextLong();
                        scanner.nextLine(); // Consume newline
                        List<CarDTO> carsWithPriceLowerThan = carController.getCarsWithPriceLowerThan(maxPrice);
                        if (!carsWithPriceLowerThan.isEmpty()) {
                            for (CarDTO car : carsWithPriceLowerThan) {
                                System.out.println(car.toString());
                            }
                        } else {
                            System.out.println("No cars found with price lower than " + maxPrice);
                        }
                        break;
                    case 7:
                        // Get all cars
                        List<CarDTO> allCars = carController.getAllCars();
                        for (CarDTO car : allCars) {
                            System.out.println(car.toString());
                        }
                        break;
                    case 8:
                        System.out.println("Exiting...");
                        exitLoop = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 8.");
                }
            } while (!exitLoop);
        }
        catch (InputMismatchException e) {
            throw new BusinessException(e.getMessage(), "handleUserInput");
        }
    }
}