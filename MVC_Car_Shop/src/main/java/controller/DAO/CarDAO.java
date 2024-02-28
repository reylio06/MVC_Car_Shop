package controller.DAO;

import exceptions.BusinessException;
import model.entity.Car;
import model.entity.FuelType;

import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.text.ParseException;

public class CarDAO {
    private static final String CSV_FILE_PATH = "src/main/resources/cars.csv";

    // Date format for parsing and formatting production dates
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    // Read cars from CSV file
    public static List<Car> readCars() throws BusinessException {
        List<Car> cars = new ArrayList<>();
        File file = new File(CSV_FILE_PATH);

        // If the file does not exist, return an empty list
        if (!file.exists()) {
            return cars;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {

                // Split the CSV line into parts
                String[] parts = line.split(", ");
                int id = Integer.parseInt(parts[0]);
                String manufacturer = parts[1];
                String model = parts[2];
                long price = Long.parseLong(parts[3]);
                // Parse date string
                Date productionDate = DATE_FORMAT.parse(parts[4]);
                Timestamp timestamp = new Timestamp(productionDate.getTime());
                FuelType fuelType = FuelType.valueOf(parts[5].toUpperCase());
                int horsepower = Integer.parseInt(parts[6]);
                // Create a new Car object and add it to the list
                cars.add(new Car(id, manufacturer, model, price, timestamp, fuelType, horsepower));
            }
        } catch (IOException | ParseException e) {
            throw new BusinessException(e.getMessage(), "readCars");
        }
        return cars;
    }

    // Write cars to CSV file
    public static void writeCars(List<Car> cars) throws BusinessException {
        File file = new File(CSV_FILE_PATH);
        File parentDirectory = file.getParentFile();

        // Check if the parent directory does not exist
        if (!parentDirectory.exists()) {

            // Attempt to create parent directories
            boolean directoriesCreated = parentDirectory.mkdirs();

            // Check if directories were created successfully
            if (!directoriesCreated) {
                // Handle failure to create directories
                throw new BusinessException("Failed to create parent directories.", "writeToFile");
            }
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            for (Car car : cars) {
                // Format the production date as a string
                String productionDateString = DATE_FORMAT.format(new Date(car.productionDate.getTime()));
                // Write the car data to the file
                bw.write(String.format("%d, %s, %s, %d, %s, %s, %d\n", car.id, car.manufacturer, car.model,
                        car.price, productionDateString, car.fuelType.toString(), car.horsepower));
            }
        } catch (IOException e) {
            throw new BusinessException(e.getMessage(), "writeCars");
        }
    }
}