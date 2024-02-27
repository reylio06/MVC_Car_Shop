package model.entity;

import model.DTO.CarDTO;
import model.DTO.CarDetailDTO;
import model.DTO.CarManufacturerDTO;

import java.sql.Timestamp;

public class Car extends Vehicle {

    public String manufacturer;
    public String model;
    public FuelType fuelType;
    public int horsepower;
    private static int nextId = 0; // Static counter variable for assigning unique IDs

    public Car(int id, String manufacturer, String model, long price, Timestamp productionDate, FuelType fuelType, int horsepower) {
        super(id, productionDate, price);
        this.id = nextId++;
        this.manufacturer = manufacturer;
        this.model = model;
        this.fuelType = fuelType;
        this.horsepower = horsepower;

    }

    // Convert entity to DTO methods
    public CarDetailDTO toCarDetailDTO() {
        return new CarDetailDTO(id, manufacturer, model, price, productionDate, fuelType, horsepower);
    }

    public CarManufacturerDTO toCarManufacturerDTO() {
        return new CarManufacturerDTO(id, model, price);
    }

    public CarDTO toCarDTO() {
        return new CarDTO(id, manufacturer, model, price);
    }
}
