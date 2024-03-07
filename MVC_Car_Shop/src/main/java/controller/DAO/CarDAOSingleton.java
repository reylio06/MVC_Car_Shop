package controller.DAO;
import exceptions.BusinessException;
import model.entity.Car;
import model.entity.FuelType;

import java.io.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CarDAOSingleton {
    private static CarDAOSingleton instance;
    private static final String CSV_FILE_PATH = "src/main/resources/cars.csv";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    private CarDAOSingleton() {} // Private constructor to prevent instantiation from outside

    public static CarDAOSingleton getInstance() {
        if (instance == null) {
            synchronized (CarDAOSingleton.class) {
                if (instance == null) {
                    instance = new CarDAOSingleton();
                }
            }
        }
        return instance;
    }

    public List<Car> readCars() throws BusinessException {
        List<Car> cars = new ArrayList<>();
        File file = new File(CSV_FILE_PATH);

        if (!file.exists()) {
            return cars;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                int id = Integer.parseInt(parts[0]);
                String manufacturer = parts[1];
                String model = parts[2];
                long price = Long.parseLong(parts[3]);
                Date productionDate = DATE_FORMAT.parse(parts[4]);
                Timestamp timestamp = new Timestamp(productionDate.getTime());
                FuelType fuelType = FuelType.valueOf(parts[5].toUpperCase());
                int horsepower = Integer.parseInt(parts[6]);
                cars.add(new Car(id, manufacturer, model, price, timestamp, fuelType, horsepower));
            }
        } catch (IOException | ParseException e) {
            throw new BusinessException(e.getMessage(), "readCars");
        }
        return cars;
    }

    public void writeCars(List<Car> cars) throws BusinessException {
        File file = new File(CSV_FILE_PATH);
        File parentDirectory = file.getParentFile();

        if (!parentDirectory.exists()) {
            boolean directoriesCreated = parentDirectory.mkdirs();
            if (!directoriesCreated) {
                throw new BusinessException("Failed to create parent directories.", "writeToFile");
            }
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            for (Car car : cars) {
                String productionDateString = DATE_FORMAT.format(new Date(car.getProductionDate().getTime()));
                bw.write(String.format("%d, %s, %s, %d, %s, %s, %d\n", car.getId(), car.getManufacturer(), car.getModel(),
                        car.getPrice(), productionDateString, car.getFuelType().toString(), car.getHorsepower()));
            }
        } catch (IOException e) {
            throw new BusinessException(e.getMessage(), "writeCars");
        }
    }
}

