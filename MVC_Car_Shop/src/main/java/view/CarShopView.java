package view;

import exceptions.BusinessException;
import controller.CarController;

import java.util.InputMismatchException;
import java.util.Scanner;

public class CarShopView {
    // Controller for managing car data
    private final CarController carController;
    private Scanner scanner;

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
                    throw new BusinessException("Choice must be a valid int value.", "handleUserInput");
                }

                switch (choice) {

                    case 1:

                        carController.addCar();
                        break;
                    case 2:

                        carController.updateCar();
                        break;
                    case 3:

                        errorMessage = carController.deleteCar();
                        break;
                    case 4:

                        carController.getCarDetails();
                        break;
                    case 5:

                        carController.getCarsByManufacturer();
                        break;
                    case 6:

                        carController.getCarsWithPriceLowerThan();
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
            } while (!exitLoop);
        }
        catch (InputMismatchException e) {
            throw new BusinessException(errorMessage, operationName);
        }
    }
}